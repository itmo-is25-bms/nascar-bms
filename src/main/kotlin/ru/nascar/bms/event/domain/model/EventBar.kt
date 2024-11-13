package ru.nascar.bms.event.domain.model

import java.time.Instant
import java.util.Objects

class EventBar(
    val id: String,
    val eventId: String,
    val barId: String,
    val createdBy: String,
    val createdAt: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventBar) return false

        return eventId == other.eventId && barId == other.barId
    }

    override fun hashCode(): Int {
        return Objects.hash(eventId, barId)
    }
}