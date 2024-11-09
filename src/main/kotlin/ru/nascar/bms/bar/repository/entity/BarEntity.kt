package ru.nascar.bms.bar.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.OffsetDateTime

@Entity
@Table(
    name = "bars",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name", "address"])
    ]
)
class BarEntity(
    @Id val id: String,
    val name: String,
    val address: String,
    val createdBy: String,
    val createdAt: OffsetDateTime,
    val updatedBy: String,
    val updatedAt: OffsetDateTime,
) {
    constructor(
        id: String,
        name: String,
        address: String,
        createdBy: String,
        createdAt: OffsetDateTime
    ) : this(
            id = id,
            name = name,
            address = address,
            createdAt = createdAt,
            createdBy = createdBy,
            updatedAt = createdAt,
            updatedBy = createdBy
        )
}
