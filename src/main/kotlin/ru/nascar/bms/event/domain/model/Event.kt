package ru.nascar.bms.event.domain.model

import ru.nascar.bms.event.domain.factories.EventParticipantFactory
import java.time.Instant

class Event(
    val id: String,
    val name: String,
    val status: EventStatus,
    val passcode: String,
    val startDateTime: Instant,
    val eventBars: List<String>,
    val participants: List<EventParticipant>,
    val createdBy: String,
    val createdAt: Instant,
    val updatedBy: String,
    val updatedAt: Instant,
) {
    fun addUser(userId: String) {
        if (participants.any { participant -> participant.userId == userId }) {
            return
        }

        val participant = EventParticipantFactory.createNew(eventId = id, userId = userId)

        participants.plus(participant)
    }
}