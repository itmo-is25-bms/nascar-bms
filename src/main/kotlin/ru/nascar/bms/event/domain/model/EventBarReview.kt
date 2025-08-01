package ru.nascar.bms.event.domain.model

import java.time.Instant
import java.util.Objects

class EventBarReview(
    val id: String,
    val eventId: String,
    val barId: String,
    var score: Int,
    var comment: String?,
    val createdBy: String,
    val createdAt: Instant,
    var updatedBy: String,
    var updatedAt: Instant,
) {
    companion object {
        const val MIN_SCORE = 1
        const val MAX_SCORE = 5
    }

    fun update(score: Int, comment: String?, updatedBy: String, updatedAt: Instant) {
        this.score = score
        this.comment = comment
        this.updatedBy = updatedBy
        this.updatedAt = updatedAt
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventBarReview) return false

        return eventId == other.eventId && barId == other.barId && createdBy == other.createdBy
    }

    override fun hashCode(): Int {
        return Objects.hash(eventId, barId, createdBy)
    }
}