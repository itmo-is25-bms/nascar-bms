package ru.nascar.bms.bar

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.nascar.bms.NascarBmsIntegrationTest
import ru.nascar.bms.bar.repository.BarRepository
import ru.nascar.bms.bar.repository.entity.BarEntity
import java.time.Instant

@Deprecated("just for example")
class ExampleBarTest(
    @Autowired private val barRepository: BarRepository
) : NascarBmsIntegrationTest() {
    @DatabaseSetup("/db/bar/before.xml")
    @ExpectedDatabase(
        value = "/db/bar/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @Test
    fun `save bar - persistence works fine`() {
        val bar = BarEntity(
            id = "BAR-01",
            name = "Рядом",
            address = "Гороховая, 32",
            createdBy = "nerud1502",
            createdAt = Instant.now(clock)
        )
        barRepository.save(bar)
    }
}
