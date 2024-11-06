package ru.nascar.bms.events

import org.springframework.data.repository.CrudRepository
import ru.nascar.bms.events.contracts.Event

interface EventRepository : CrudRepository<Event, String> {
    fun getById(id: String): Event
    fun findAllByCreatedBy(createdBy: String): Iterable<Event>
    fun findByPasscode(passcode: String): Event?
}