package ru.nascar.bms.event.service

import ru.nascar.bms.event.service.model.EventInternal
import java.time.Instant

interface EventService {
    fun create(userId: String, name: String, startDatetime: Instant, eventBarsIds: Set<String>): EventInternal
    fun getById(id: String): EventInternal
    fun findByPasscode(passcode: String): EventInternal?
    fun getByUserId(userId: String): List<EventInternal>
    fun addUserToEvent(id: String, userId: String)
    fun removeUserFromEvent(id: String, userId: String)
}