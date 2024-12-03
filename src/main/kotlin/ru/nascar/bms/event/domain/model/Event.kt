package ru.nascar.bms.event.domain.model

import ru.nascar.bms.event.domain.exception.BarNotFoundInEventException
import ru.nascar.bms.event.domain.exception.EventAuthorRemovalException
import ru.nascar.bms.event.domain.exception.InvalidEventIdException
import ru.nascar.bms.event.domain.exception.InvalidEventStatusException
import ru.nascar.bms.event.domain.exception.ReceiptAlreadyExistsException
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

        participants += user
    }

    fun removeUser(user: EventParticipant) {
        ensureValidEventId(user.eventId)
        ensureNotAuthorRemoval(user.userId)
        ensureValidParticipantId(user.userId)

        participants -= user
    }

    fun start(startedBy: String, startedAt: Instant) {
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

    private fun ensureCanAddReceiptForBar(barId: String) {
        ensureValidBarId(barId)

        if (status != EventStatus.FINISHED) {
            throw InvalidEventStatusException.create(
                eventId = id,
                eventStatus = status,
                actionName = "AddReceipt"
            )
        }

        if (receipts.any { it.barId == barId }) {
            throw ReceiptAlreadyExistsException.create(eventId = id, barId = barId)
        }
    }

    fun addReceipt(receipt: EventReceipt) {
        ensureValidEventId(receipt.eventId)
        ensureValidParticipantId(receipt.createdBy)
        ensureCanAddReceiptForBar(receipt.barId)

        receipts += receipt
    }

    fun addReview(review: EventBarReview) {
        ensureValidEventId(review.eventId)
        ensureValidBarId(review.barId)
        ensureValidParticipantId(review.createdBy)

        val existingReview = reviews.firstOrNull { it == review }

        if (existingReview == null) {
            reviews += review
        } else {
            existingReview.update(
                score = review.score,
                comment = review.comment,
                updatedBy = review.updatedBy,
                updatedAt = review.updatedAt,
            )
        }
    }

    private fun ensureValidEventId(eventId: String) {
        if (id != eventId) {
            throw InvalidEventIdException.create(eventId = id, invalidEventId = eventId)
        }
    }

    private fun ensureValidBarId(barId: String) {
        if (eventBars.none { it.barId == barId }) {
            throw BarNotFoundInEventException.create(eventId = id, barId = barId)
        }
    }

    private fun ensureValidParticipantId(userId: String) {
        if (participants.none { it.userId == userId }) {
           throw UserNotFoundInEventException.create(eventId = id, userId = userId)
        }
    }

    private fun ensureUserIsAuthor(userId: String) {
        if (createdBy != userId) {
            throw UnauthorizedEventActionException.create(eventId = id, userId = userId)
        }
    }

    private fun ensureNotAuthorRemoval(userId: String) {
        if (createdBy == userId) {
            throw EventAuthorRemovalException.forEventAndAuthor(id, userId)
        }
    }
}