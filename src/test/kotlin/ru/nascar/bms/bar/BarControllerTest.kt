package ru.nascar.bms.bar

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import io.grpc.Status.Code
import io.grpc.StatusRuntimeException
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.nascar.bms.NascarBmsIntegrationTest
import ru.nascar.bms.presentation.abstractions.BarServiceProto

private const val BAR_NAME = "Рядом"
private const val BAR_ADDRESS = "Гороховая, 32"

class BarControllerTest: NascarBmsIntegrationTest() {
    @Test
    @DatabaseSetup("/controller/bar/create/happy_path/before.xml")
    @ExpectedDatabase(
        value = "/controller/bar/create/happy_path/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create bar - happy path - ok`() {
        // arrange
        val request = getCreateBarRequest()

        // act
        val response = barServiceGrpc.create(request)

        // assert
        assertThat(response.bar.name).isEqualTo(request.bar.name)
        assertThat(response.bar.address).isEqualTo(request.bar.address)
    }

    @Test
    @DatabaseSetup("/controller/bar/create/bar_with_name_and_address_exists/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/bar/create/bar_with_name_and_address_exists/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create bar - bar with given name and address already exists - error`() {
        // arrange
        val request = getCreateBarRequest()

        // act & assert
        val statusException = assertThrows<StatusRuntimeException> {
            barServiceGrpc.create(request)
        }

        assertThat(statusException.status.code).isEqualTo(Code.ABORTED)
        assertThat(statusException.status.description).isEqualTo(
            "Bar with name ${request.bar.name} and address ${request.bar.address} already exists"
        )
    }

    private fun getCreateBarRequest(): BarServiceProto.CreateCommand {
        val bar = BarServiceProto.CreateCommandBarDto.newBuilder()
            .setName(BAR_NAME)
            .setAddress(BAR_ADDRESS)
            .build()

        return BarServiceProto.CreateCommand.newBuilder()
            .setBar(bar)
            .setUserId(USER_ID)
            .build()
    }
}
