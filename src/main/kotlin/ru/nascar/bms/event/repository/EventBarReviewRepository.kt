package ru.nascar.bms.event.repository

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventBarReview

interface EventBarReviewRepository {
    fun findAllByEventId(eventId: String): List<EventBarReview>
    fun saveAllFromEvent(event: Event)
    fun save(eventBarReview: EventBarReview)
    fun delete(eventBarReviewId: String)
}