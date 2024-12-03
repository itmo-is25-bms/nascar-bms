package ru.nascar.bms.event.service.model

import ru.nascar.bms.event.domain.model.EventBarReview

data class EventBarReviewInternal(
    val userId: String,
    val score: Int,
    val comment: String?
) {
    companion object {
        fun fromDomain(eventBarReview: EventBarReview): EventBarReviewInternal {
            return EventBarReviewInternal(
                userId = eventBarReview.createdBy,
                score = eventBarReview.score,
                comment = eventBarReview.comment,
            )
        }
    }
}