package ru.nascar.bms.event.domain.model

import java.time.Instant

class EventBar(
    val id: String,
    val eventId: String,
    val barId: String,
    val createdBy: String,
    val createdAt: Instant,
)