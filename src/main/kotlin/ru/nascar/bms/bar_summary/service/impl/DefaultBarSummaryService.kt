package ru.nascar.bms.bar_summary.service.impl

import org.springframework.stereotype.Service
import ru.nascar.bms.bar_summary.domain.BarSummary
import ru.nascar.bms.bar_summary.domain.ReviewEntry
import ru.nascar.bms.bar_summary.external.SummarizerClient
import ru.nascar.bms.bar_summary.repository.BarSummaryRepository
import ru.nascar.bms.bar_summary.service.BarSummaryService

@Service
class DefaultBarSummaryService(
    private val barSummaryRepository: BarSummaryRepository,
    private val summarizerClient: SummarizerClient
) : BarSummaryService {
    override fun updateBarSummary(barId: String) {
        val reviews = barSummaryRepository.getReviewsForBar(barId = barId)

        if (reviews.isEmpty()) {
            return
        }

        val score = calculateScore(reviewEntries = reviews)
        val summaryTags = getSummaryTags(reviewEntries = reviews)

        val barSummary = BarSummary(
            score = score,
            summaryTags = summaryTags
        )

        barSummaryRepository.updateBarSummary(barId = barId, summary = barSummary)
    }

    private fun getSummaryTags(reviewEntries: List<ReviewEntry>): List<String> {
        return summarizerClient.getSummaryTags(reviews = reviewEntries)
    }

    private fun calculateScore(reviewEntries: List<ReviewEntry>): Double {
        return reviewEntries.map { it -> it.score }.average()
    }
}