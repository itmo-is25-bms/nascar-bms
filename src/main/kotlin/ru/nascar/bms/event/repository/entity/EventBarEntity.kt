package ru.nascar.bms.event.repository.entity

import java.time.Instant

class EventBarEntity(
    val id: String,
    val eventId: String,
    val barId: String,
    val createdBy: String,
    val createdAt: Instant,
)