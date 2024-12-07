package ru.nascar.bms.bar_summary.repository

import ru.nascar.bms.bar_summary.domain.BarSummary
import ru.nascar.bms.bar_summary.domain.ReviewEntry

interface BarSummaryRepository {
    fun getReviewsForBar(barId: String): List<ReviewEntry>

    fun updateBarSummary(barId: String, summary: BarSummary)
}