package ru.nascar.bms.event.domain.model

import ru.nascar.bms.event.domain.factories.EventParticipantFactory
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
        if (participants.any { participant -> participant.userId == user.userId }) {
            return
        }

        participants.plus(user)
    }
}