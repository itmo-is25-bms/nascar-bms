package ru.nascar.bms.receipt.domain.model

import java.time.Instant

class Receipt(
    val id: String,
    val receiptData: ByteArray,
    val createdBy: String,
    val createdAt: Instant,
)