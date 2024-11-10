package ru.nascar.bms.event.repository.entity

import java.time.Instant

class EventParticipantEntity(
    val id: String,
    val eventId: String,
    val userId: String,
    val joinedAt: Instant,
    val createdBy: String,
    val createdAt: Instant,
)