package ru.nascar.bms.bar_summary.repository.entity

import java.time.Instant

class ReviewEntryEntity(
    val score: Int,
    val comment: String?,
    val timestamp: Instant,
)