package ru.nascar.bms.event.domain.factories

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventParticipant
import ru.nascar.bms.event.domain.model.EventStatus
import java.time.Instant
import java.util.UUID

class EventFactory {
    companion object {
        fun createNew(
            name: String,
            passcode: String,
            startDateTime: Instant,
            eventBars: List<String>,
            createdBy: String,
            createdAt: Instant,
        ): Event {
            val id = "event-" + UUID.randomUUID().toString()
            val author = EventParticipantFactory.createNew(
                eventId = id,
                userId = createdBy,
                createdAt = createdAt,
                createdBy = createdBy
            )
            return Event(
                id = id,
                name = name,
                status = EventStatus.CREATED,
                passcode = passcode,
                startDateTime = startDateTime,
                eventBars = eventBars,
                participants = listOf(author),
                createdAt = createdAt,
                createdBy = createdBy,
                updatedAt = createdAt,
                updatedBy = createdBy,
            )
        }

        fun createFromDb(
            id: String,
            name: String,
            status: EventStatus,
            passcode: String,
            startDateTime: Instant,
            eventBars: List<String>,
            participants: List<EventParticipant>,
            createdBy: String,
            createdAt: Instant,
            updatedBy: String,
            updatedAt: Instant,
        ): Event {
            return Event(
                id = id,
                name = name,
                status = status,
                passcode = passcode,
                startDateTime = startDateTime,
                eventBars = eventBars,
                participants = participants,
                createdAt = createdAt,
                createdBy = createdBy,
                updatedAt = updatedAt,
                updatedBy = updatedBy,
            )
        }
    }
}