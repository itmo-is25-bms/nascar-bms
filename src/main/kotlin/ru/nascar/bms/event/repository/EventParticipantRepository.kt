package ru.nascar.bms.event.repository

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventParticipant

interface EventParticipantRepository {
    fun findAllByEventId(eventId: String): List<EventParticipant>
    fun saveAllFromEvent(event: Event)
}