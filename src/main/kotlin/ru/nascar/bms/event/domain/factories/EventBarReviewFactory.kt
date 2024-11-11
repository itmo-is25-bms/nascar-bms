package ru.nascar.bms.event.domain.factories

import ru.nascar.bms.event.domain.model.EventBarReview
import java.time.Instant
import java.util.UUID

class EventBarReviewFactory {
    companion object {
        fun createNew(
            eventId: String,
            barId: String,
            score: Int,
            comment: String,
            createdBy: String,
            createdAt: Instant,
        ): EventBarReview {
            val id = "event-bar-review-" + UUID.randomUUID().toString()
            return EventBarReview(
                id = id,
                eventId = eventId,
                barId = barId,
                score = score,
                comment = comment,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }

        fun createFromDb(
            id: String,
            eventId: String,
            barId: String,
            score: Int,
            comment: String,
            createdBy: String,
            createdAt: Instant,
        ): EventBarReview {
            return EventBarReview(
                id = id,
                eventId = eventId,
                barId = barId,
                score = score,
                comment = comment,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }
    }
}