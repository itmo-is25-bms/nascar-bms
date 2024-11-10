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
}