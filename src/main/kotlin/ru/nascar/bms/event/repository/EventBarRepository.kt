package ru.nascar.bms.event.repository

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventBar

interface EventBarRepository {
    fun findAllByEventId(eventId: String): List<EventBar>
    fun saveAllFromEvent(event: Event)
    fun save(eventBar: EventBar)
    fun delete(eventBarId: String)
}