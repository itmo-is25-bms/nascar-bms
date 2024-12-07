package ru.nascar.bms.bar_summary.domain

data class BarSummary(
    val score: Double,
    val summaryTags: List<String>,
)
