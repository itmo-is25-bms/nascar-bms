package ru.nascar.bms.event.service

import ru.nascar.bms.event.service.model.EventInternal
import java.time.Instant

interface EventService {
    fun create(userId: String, name: String, startDatetime: Instant, eventBars: List<String>): EventInternal
    fun getById(id: String): EventInternal
    fun getByPasscode(passcode: String): EventInternal?

    fun addUserToEvent(id: String, userId: String)
}