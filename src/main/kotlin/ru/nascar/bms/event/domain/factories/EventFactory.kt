package ru.nascar.bms.event.domain.factories

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventBar
import ru.nascar.bms.event.domain.model.EventBarReview
import ru.nascar.bms.event.domain.model.EventParticipant
import ru.nascar.bms.event.domain.model.EventReceipt
import ru.nascar.bms.event.domain.model.EventStatus
import java.time.Instant
import java.util.UUID

class EventFactory {
    companion object {
        fun createNew(
            name: String,
            passcode: String,
            startDateTime: Instant,
            eventBarsIds: List<String>,
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
            val eventBars = eventBarsIds.map { barId -> EventBarFactory.createNew(
                eventId = id,
                barId = barId,
                createdBy = createdBy,
                createdAt = createdAt,
            ) }
            return Event(
                id = id,
                name = name,
                status = EventStatus.CREATED,
                passcode = passcode,
                startDateTime = startDateTime,
                eventBars = eventBars,
                participants = listOf(author),
                receipts = listOf(),
                reviews = listOf(),
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
            eventBars: List<EventBar>,
            participants: List<EventParticipant>,
            receipts: List<EventReceipt>,
            reviews: List<EventBarReview>,
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
                receipts = receipts,
                reviews = reviews,
                createdAt = createdAt,
                createdBy = createdBy,
                updatedAt = updatedAt,
                updatedBy = updatedBy,
            )
        }
    }
}