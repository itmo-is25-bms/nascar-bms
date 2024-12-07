package ru.nascar.bms.bar.domain.model

data class Bar(
    val id: String,
    val name: String,
    val address: String,
    val summaryTags: List<String>?,
    val score: Double?,
)
