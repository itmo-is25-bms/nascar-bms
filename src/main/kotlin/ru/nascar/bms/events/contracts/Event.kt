package ru.nascar.bms.events.contracts

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "events")
class Event(
    @Id
    val id: String,
    val name: String,
    val status: String,
    val passcode: String,
    val startDateTime: Date,
    val createdBy: String,
    val createdAt: Date,
    val updatedBy: String,
    val updatedAt: Date,
)