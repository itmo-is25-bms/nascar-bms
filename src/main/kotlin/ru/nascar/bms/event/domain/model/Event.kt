package ru.nascar.bms.event.domain.model

import java.time.Instant

class Event(
    val id: String,
    val name: String,
    var status: EventStatus,
    val passcode: String,
    val startDateTime: Instant,
    val eventBars: List<EventBar>,
    val participants: List<EventParticipant>,
    val receipts: List<EventReceipt>,
    val reviews: List<EventBarReview>,
    val createdBy: String,
    val createdAt: Instant,
    var updatedBy: String,
    var updatedAt: Instant,
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

    fun start(startedBy: String, startedAt: Instant) {
        if (status != EventStatus.CREATED) {
            // TODO: throw
            return
        }

        if (createdBy != startedBy) {
            // TODO: throw
            return
        }

        status = EventStatus.IN_PROGRESS
        updatedBy = startedBy
        updatedAt = startedAt
    }

    fun finish(finishedBy: String, finishedAt: Instant) {
        if (status != EventStatus.IN_PROGRESS) {
            // TODO: throw
            return
        }

        if (createdBy != finishedBy) {
            // TODO: throw
            return
        }

        status = EventStatus.FINISHED
        updatedBy = finishedBy
        updatedAt = finishedAt
    }

    fun addReceipt(receipt: EventReceipt) {
        // TODO: run all checks
        receipts.plus(receipt)
    }

    fun addReview(review: EventBarReview) {
        // TODO: run all checks
        reviews.plus(review)
    }
}