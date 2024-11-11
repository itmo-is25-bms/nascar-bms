package ru.nascar.bms.receipt.repository.entity

import java.time.Instant

class ReceiptEntity(
    val id: String,
    val eventId: String,
    val barId: String,
    val receiptId: String,
    val createdBy: String,
    val createdAt: Instant,
)