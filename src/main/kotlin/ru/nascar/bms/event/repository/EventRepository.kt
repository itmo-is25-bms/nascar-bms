package ru.nascar.bms.event.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.nascar.bms.event.repository.entity.EventEntity

interface EventRepository : JpaRepository<EventEntity, String> {
    fun findAllByCreatedBy(createdBy: String): List<EventEntity>
    fun findByPasscode(passcode: String): EventEntity?
}
