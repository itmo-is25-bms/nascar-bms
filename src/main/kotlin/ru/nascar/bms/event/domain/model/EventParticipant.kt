package ru.nascar.bms.event.domain.model

import java.time.Instant

class EventParticipant(
    val id: String,
    val eventId: String,
    val userId: String,
    val joinedAt: Instant,
    val createdBy: String,
    val createdAt: Instant,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventParticipant) return false

        return eventId == other.eventId && userId == other.userId
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}