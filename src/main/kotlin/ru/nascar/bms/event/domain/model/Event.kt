package ru.nascar.bms.event.domain.model

import java.time.Instant

class Event(
    val id: String,
    val name: String,
    val status: EventStatus,
    val passcode: String,
    val startDateTime: Instant,
    val eventBars: List<EventBar>,
    val participants: List<EventParticipant>,
    val createdBy: String,
    val createdAt: Instant,
    val updatedBy: String,
    val updatedAt: Instant,
) {
    fun addUser(user: EventParticipant) {
        if (user in participants) {
            // TODO: Or throw 'AlreadyExists'?
            return
        }

        participants.plus(user)
    }

    fun removeUser(user: EventParticipant) {
        if (user !in participants) {
            // TODO: Or throw 'NotFound'?
            return
        }

        participants.minus(user)
    }
}