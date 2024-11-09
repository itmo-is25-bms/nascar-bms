package ru.nascar.bms.event.repository.entity

import java.time.Instant

class EventEntity(
    val id: String,
    val name: String,
    val status: String,
    val passcode: String,
    val startDateTime: Instant,
    val createdBy: String,
    val createdAt: Instant,
    val updatedBy: String,
    val updatedAt: Instant,
)