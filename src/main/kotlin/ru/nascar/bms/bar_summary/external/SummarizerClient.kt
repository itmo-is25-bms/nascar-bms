package ru.nascar.bms.bar_summary.external

import ru.nascar.bms.bar_summary.domain.ReviewEntry

interface SummarizerClient {
    fun getSummaryTags(reviews: List<ReviewEntry>, tagsQuantity: Int = 5, maxFeatures: Int = 10000): List<String>
}