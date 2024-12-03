package ru.nascar.bms.event.service.model

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventBar

data class EventBarInternal(
    val id: String,
    val reviews: List<EventBarReviewInternal>
) {
    companion object {
        fun fromDomain(eventBar: EventBar, event: Event): EventBarInternal {
            val reviews = event.reviews
                .filter { it.barId == eventBar.barId }
                .map { EventBarReviewInternal.fromDomain(it) }

            return EventBarInternal(
                id = eventBar.barId,
                reviews = reviews,
            )
        }
    }
}