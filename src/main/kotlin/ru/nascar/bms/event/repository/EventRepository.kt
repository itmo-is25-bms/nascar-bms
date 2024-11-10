package ru.nascar.bms.event.repository

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.repository.entity.EventEntity

interface EventRepository {
    fun findAllByCreatedBy(createdBy: String): List<Event>
    fun findByPasscode(passcode: String): Event?
    fun findById(id: String): Event?
    fun save(event: Event)
}
