package ru.nascar.bms.event.domain.model

import ru.nascar.bms.event.domain.exception.BarNotFoundInEventException
import ru.nascar.bms.event.domain.exception.InvalidEventIdException
import ru.nascar.bms.event.domain.exception.InvalidEventStatusException
import ru.nascar.bms.event.domain.exception.ReceiptAlreadyExistsException
import ru.nascar.bms.event.domain.exception.ReviewAlreadyExistsException
import ru.nascar.bms.event.domain.exception.UnauthorizedEventActionException
import ru.nascar.bms.event.domain.exception.UserAlreadyExistsException
import ru.nascar.bms.event.domain.exception.UserNotFoundInEventException
import java.time.Instant

class Event(
    val id: String,
    val name: String,
    var status: EventStatus,
    val passcode: String,
    val startDateTime: Instant,
    var eventBars: List<EventBar>,
    var participants: List<EventParticipant>,
    var receipts: List<EventReceipt>,
    var reviews: List<EventBarReview>,
    val createdBy: String,
    val createdAt: Instant,
    var updatedBy: String,
    var updatedAt: Instant,
) {
    fun addUser(user: EventParticipant) {
        if (user in participants) {
            throw UserAlreadyExistsException.create(eventId = id, userId = user.userId)
        }

        participants = participants.plus(user)
    }

    fun removeUser(user: EventParticipant) {
        ensureValidEventId(eventId = user.eventId)
        ensureValidParticipantId(userId = user.userId)

        participants = participants.minus(user)
    }

    fun start(startedBy: String, startedAt: Instant) {
        ensureValidParticipantId(startedBy)
        ensureUserIsAuthor(startedBy)

        if (status != EventStatus.CREATED) {
            throw InvalidEventStatusException.create(
                eventId = id,
                eventStatus = status,
                actionName = "StartEvent"
            )
        }

        status = EventStatus.IN_PROGRESS
        updatedBy = startedBy
        updatedAt = startedAt
    }

    fun finish(finishedBy: String, finishedAt: Instant) {
        ensureValidParticipantId(finishedBy)
        ensureUserIsAuthor(finishedBy)

        if (status != EventStatus.IN_PROGRESS) {
            throw InvalidEventStatusException.create(
                eventId = id,
                eventStatus = status,
                actionName = "FinishEvent"
            )
        }

        status = EventStatus.FINISHED
        updatedBy = finishedBy
        updatedAt = finishedAt
    }

    fun ensureCanAddReceiptForBar(barId: String) {
        ensureValidBarId(barId)

        if (status != EventStatus.FINISHED) {
            throw InvalidEventStatusException.create(
                eventId = id,
                eventStatus = status,
                actionName = "AddReceipt"
            )
        }

        if (receipts.any { receipt -> receipt.barId == barId}) {
            throw ReceiptAlreadyExistsException.create(eventId = id, barId = barId)
        }
    }

    fun addReceipt(receipt: EventReceipt) {
        ensureValidEventId(receipt.eventId)
        ensureValidParticipantId(receipt.createdBy)
        ensureCanAddReceiptForBar(barId = receipt.barId)

        receipts = receipts.plus(receipt)
    }

    fun addReview(review: EventBarReview) {
        ensureValidEventId(review.eventId)
        ensureValidBarId(review.barId)
        ensureValidParticipantId(review.createdBy)

        if (review in reviews) {
            throw ReviewAlreadyExistsException.create(eventId = id, barId = review.barId, userId = review.createdBy)
        }

        reviews = reviews.plus(review)
    }

    private fun ensureValidEventId(eventId: String) {
        if (id != eventId) {
            throw InvalidEventIdException.create(eventId = id, invalidEventId = eventId)
        }
    }

    private fun ensureValidBarId(barId: String) {
        if (!eventBars.any { eventBar -> eventBar.barId == barId }) {
            throw BarNotFoundInEventException.create(eventId = id, barId = barId)
        }
    }

    private fun ensureValidParticipantId(userId: String) {
        if (!participants.any { participant -> participant.userId == userId }) {
           throw UserNotFoundInEventException.create(eventId = id, userId = userId)
        }
    }

    private fun ensureUserIsAuthor(userId: String) {
        if (createdBy != userId) {
            throw UnauthorizedEventActionException.create(eventId = id, userId = userId)
        }
    }
}