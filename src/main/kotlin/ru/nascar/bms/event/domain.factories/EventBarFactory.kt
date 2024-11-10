package ru.nascar.bms.event.domain.factories

import ru.nascar.bms.event.domain.model.EventBar
import java.time.Instant
import java.util.UUID

class EventBarFactory {
    companion object {
        fun createNew(eventId: String, barId: String, createdBy: String, createdAt: Instant): EventBar {
            val id = "event-bar-" + UUID.randomUUID().toString()
            return EventBar(
                id = id,
                eventId = eventId,
                barId = barId,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }

        fun createFromDb(id: String, eventId: String, barId: String, createdBy: String, createdAt: Instant): EventBar {
            return EventBar(
                id = id,
                eventId = eventId,
                barId = barId,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }
    }
}