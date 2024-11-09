package ru.nascar.bms.event.repository

import ru.nascar.bms.event.repository.entity.EventEntity

interface EventRepository {
    fun findAllByCreatedBy(createdBy: String): List<EventEntity>
    fun findByPasscode(passcode: String): EventEntity?
}
