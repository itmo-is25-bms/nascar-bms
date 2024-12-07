package ru.nascar.bms.bar_summary.external.impl

import ru.nascar.bms.bar_summary.domain.ReviewEntry
import ru.nascar.bms.bar_summary.external.SummarizerClient
import ru.nascar.bms.presentation.abstractions.SummaryServiceGrpc.SummaryServiceBlockingStub
import ru.nascar.bms.presentation.abstractions.SummaryServiceProto.*

class DefaultSummarizerClient(
    private val summaryClient: SummaryServiceBlockingStub
) : SummarizerClient {
    override fun getSummaryTags(reviews: List<ReviewEntry>, tagsQuantity: Int, maxFeatures: Int): List<String> {
        val reviewsDto = reviews.filter { it.comment != null }.map {
            it -> SummaryRequestEntryDto.newBuilder()
                .setText(it.comment)
                .setTimestamp(it.timestamp.epochSecond)
                .build()
        }

        if (reviewsDto.isEmpty()) {
            return listOf()
        }

        val request = SummaryRequest.newBuilder()
            .addAllReviews(reviewsDto)
            .setTagsQuantity(tagsQuantity)
            .setMaxFeatures(maxFeatures)
            .build()

        val response = summaryClient.getSummary(request)

        return response.summaryTagsList
    }
}