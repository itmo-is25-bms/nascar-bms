package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class UnauthorizedEventActionException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, userId: String): UnauthorizedEventActionException {
            return UnauthorizedEventActionException(
                "User $userId can't perform action on event $eventId",
                EventErrorCode.UNAUTHORIZED_EVENT_ACTION,
                mapOf(
                    "eventId" to eventId,
                    "userId" to userId,
                )
            )
        }
    }
}