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
) {
    constructor(
        id: String,
        name: String,
        status: String,
        passcode: String,
        startDateTime: Instant,
        createdBy: String,
        createdAt: Instant
    ) : this(
        id = id,
        name = name,
        status = status,
        passcode = passcode,
        startDateTime = startDateTime,
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = createdAt,
        updatedBy = createdBy,
    )
}