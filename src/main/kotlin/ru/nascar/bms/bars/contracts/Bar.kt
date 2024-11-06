package ru.nascar.bms.bars.contracts

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity
@Table(name = "bars")
class Bar(
    @Id val id: String,
    val name: String,
    val address: String,
    val createdBy: String,
    val createdAt: ZonedDateTime,
    val updatedBy: String,
    val updatedAt: ZonedDateTime,
) {
    companion object {
        fun createNew(name: String, address: String, userId: String): Bar {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-")
            val formattedDate = LocalDateTime.now().format(formatter)

            val barId = "bar-" + formattedDate + UUID.randomUUID()

            val now = ZonedDateTime.now()

            return Bar(
                id = barId,
                name = name,
                address = address,
                createdAt = now,
                createdBy = userId,
                updatedAt = now,
                updatedBy = userId,
            )
        }
    }
}