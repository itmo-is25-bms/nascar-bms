package ru.nascar.bms.event.domain.factories

import ru.nascar.bms.event.domain.model.EventParticipant
import java.time.Instant
import java.util.UUID

class EventParticipantFactory {
    companion object {
        fun createNew(eventId: String, userId: String, createdAt: Instant, createdBy: String): EventParticipant {
            val id = "participant-" + UUID.randomUUID().toString()
            return EventParticipant(
                id = id,
                eventId = eventId,
                userId = userId,
                joinedAt = createdAt,
                createdAt = createdAt,
                createdBy = createdBy,
            )
        }

        fun createFromDb(
            id: String,
            eventId: String,
            userId: String,
            joinedAt: Instant,
            createdAt: Instant,
            createdBy: String
        ): EventParticipant {
            return EventParticipant(
                id = id,
                eventId = eventId,
                userId = userId,
                joinedAt = joinedAt,
                createdAt = createdAt,
                createdBy = createdBy,
            )
        }
    }
}