package ru.nascar.bms.event.domain.factories

import ru.nascar.bms.event.domain.model.EventReceipt
import java.time.Instant
import java.util.UUID

class EventReceiptFactory {
    companion object {
        fun createNew(
            eventId: String,
            barId: String,
            receiptId: String,
            createdBy: String,
            createdAt: Instant,
        ): EventReceipt {
            val id = "event-receipt-" + UUID.randomUUID().toString()
            return EventReceipt(
                id = id,
                eventId = eventId,
                barId = barId,
                receiptId = receiptId,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }

        fun createFromDb(
            id: String,
            eventId: String,
            barId: String,
            receiptId: String,
            createdBy: String,
            createdAt: Instant,
        ): EventReceipt {
            return EventReceipt(
                id = id,
                eventId = eventId,
                barId = barId,
                receiptId = receiptId,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }
    }
}