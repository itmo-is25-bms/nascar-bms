package ru.nascar.bms.event.repository

import ru.nascar.bms.event.domain.model.Event

interface EventRepository {
    fun findAllByCreatedBy(createdBy: String): List<Event>
    fun findByPasscode(passcode: String): Event?
    fun findById(id: String): Event?
    fun findByIds(ids: Collection<String>): List<Event>
    fun save(event: Event)
}
