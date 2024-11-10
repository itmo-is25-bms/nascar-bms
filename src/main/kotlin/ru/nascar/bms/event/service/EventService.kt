package ru.nascar.bms.event.service

import ru.nascar.bms.event.service.model.EventInternal
import java.time.Instant

interface EventService {
    fun create(userId: String, name: String, startDatetime: Instant, eventBarsIds: List<String>): EventInternal
    fun getById(id: String): EventInternal
    fun getByPasscode(passcode: String): EventInternal?
    fun getByUserId(userId: String): List<EventInternal>

    fun addUserToEvent(id: String, userId: String)
    fun removeUserFromEvent(id: String, userId: String)
}