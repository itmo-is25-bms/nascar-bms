package ru.nascar.bms.bar.controller

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.junit.jupiter.api.Test
import ru.nascar.bms.NascarBmsIntegrationTest
import ru.nascar.bms.presentation.abstractions.BarServiceProto

private const val BAR_ID = "BAR-01"
private const val BAR_NAME = "Рядом"
private const val BAR_ADDRESS = "Гороховая, 32"

class BarControllerTest : NascarBmsIntegrationTest() {
    @Test
    @DatabaseSetup("/controller/bar/create/happy_path/before.xml")
    @ExpectedDatabase(
        value = "/controller/bar/create/happy_path/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create bar - happy path - create bar with given parameters`() {
        // arrange
        val request = getCreateBarRequest()

        // act
        val response = barServiceGrpc.create(request)

        // assert
        val expectedBar = getBarDto(
            name = request.bar.name,
            address = request.bar.address
        )
        grpcAssert.assertProtoEqualsIgnoringId(expectedBar, response.bar)
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
        grpcAssert.assertStatusException(
            message = "Bar with name ${request.bar.name} and address ${request.bar.address} already exists"
        ) {
            barServiceGrpc.create(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/bar/get_by_id/happy_path/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/bar/get_by_id/happy_path/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get bar by id - happy path - return requested bar`() {
        // arrange
        val request = getGetBarByIdRequest()

        // act
        val response = barServiceGrpc.getById(request)

        // assert
        val expectedBar = getBarDto(
            id = request.barId,
            name = BAR_NAME,
            address = BAR_ADDRESS
        )
        grpcAssert.assertProtoEquals(expectedBar, response.bar)
    }

    @Test
    @DatabaseSetup("/controller/bar/get_by_id/bar_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/bar/get_by_id/bar_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get bar by id - bar does not exist - error`() {
        // arrange
        val request = getGetBarByIdRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Bar with id ${request.barId} not found"
        ) {
            barServiceGrpc.getById(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/bar/get_all/happy_path/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/bar/get_all/happy_path/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get all bars - happy path - return all existing bars in any order`() {
        // arrange
        val request = BarServiceProto.GetAllQuery.getDefaultInstance()

        // act
        val response = barServiceGrpc.getAll(request)

        // assert
        val expectedBars = listOf(
            getBarDto(name = "Laboratorio Distilita", address = "Гражданская, 13-15", id = "BAR-00"),
            getBarDto(name = "Рядом", address = "Гороховая, 32", id = "BAR-01")
        )
        grpcAssert.assertProtoCollectionEqualsIgnoringOrder(
            expectedBars,
            response.barsList
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

    private fun getGetBarByIdRequest(): BarServiceProto.GetByIdQuery {
        return BarServiceProto.GetByIdQuery.newBuilder()
            .setBarId(BAR_ID)
            .build()
    }

    private fun getBarDto(name: String, address: String, id: String = ""): BarServiceProto.BarDto {
        return BarServiceProto.BarDto.newBuilder()
            .setId(id)
            .setName(name)
            .setAddress(address)
            .setBarSummary(BarServiceProto.BarSummaryDto.newBuilder().build())
            .build()
    }
}
