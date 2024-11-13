package ru.nascar.bms.event.domain.model

import java.time.Instant

class EventReceipt(
    val id: String,
    val eventId: String,
    val barId: String,
    val receiptId: String,
    val createdBy: String,
    val createdAt: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventReceipt) return false

        return eventId == other.eventId && barId == other.barId && receiptId == other.receiptId
    }

    override fun hashCode(): Int {
        return "$eventId$barId$receiptId".hashCode()
    }
}