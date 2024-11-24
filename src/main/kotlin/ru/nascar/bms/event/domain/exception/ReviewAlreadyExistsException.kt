package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class ReviewAlreadyExistsException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, barId: String, userId: String): ReviewAlreadyExistsException {
            return ReviewAlreadyExistsException(
                "Review for bar $barId during event $eventId by user $userId already exists",
                EventErrorCode.EVENT_BAR_REVIEW_EXISTS_BY_USER,
                mapOf(
                    "barId" to barId,
                    "eventId" to eventId,
                    "userId" to userId
                )
            )
        }
    }
}
