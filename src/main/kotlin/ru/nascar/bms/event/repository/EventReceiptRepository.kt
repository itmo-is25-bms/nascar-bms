package ru.nascar.bms.event.repository

import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventReceipt

interface EventReceiptRepository {
    fun findAllByEventId(eventId: String): List<EventReceipt>
    fun saveAllFromEvent(event: Event)
    fun save(eventReceipt: EventReceipt)
    fun delete(eventReceiptId: String)
}