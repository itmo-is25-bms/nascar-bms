package ru.nascar.bms.bar_summary.domain

import java.time.Instant

data class ReviewEntry(
    val score: Int,
    val comment: String?,
    val timestamp: Instant,
)