package ru.nascar.bms.bar.repository.entity

import java.time.Instant

class BarEntity(
    val id: String,
    val name: String,
    val address: String,
    val createdBy: String,
    val createdAt: Instant,
    val updatedBy: String,
    val updatedAt: Instant,
    val summaryTags: List<String>?,
    val score: Double?,
) {
    constructor(
        id: String,
        name: String,
        address: String,
        createdBy: String,
        createdAt: Instant,
    ) : this(
            id = id,
            name = name,
            address = address,
            createdAt = createdAt,
            createdBy = createdBy,
            updatedAt = createdAt,
            updatedBy = createdBy,
            summaryTags = null,
            score = null,
        )
}
