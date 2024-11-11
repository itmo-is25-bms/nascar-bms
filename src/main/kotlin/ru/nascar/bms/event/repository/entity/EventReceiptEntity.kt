package ru.nascar.bms.event.repository.entity

import java.time.Instant

class EventReceiptEntity(
    val id: String,
    val eventId: String,
    val barId: String,
    val receiptId: String,
    val createdBy: String,
    val createdAt: Instant,
)