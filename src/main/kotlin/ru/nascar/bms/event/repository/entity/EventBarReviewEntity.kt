package ru.nascar.bms.event.repository.entity

import java.time.Instant

class EventBarReviewEntity(
    val id: String,
    val eventId: String,
    val barId: String,
    val score: Int,
    val comment: String,
    val createdBy: String,
    val createdAt: Instant,
)