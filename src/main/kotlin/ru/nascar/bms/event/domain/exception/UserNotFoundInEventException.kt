package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class UserNotFoundInEventException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, userId: String): UserNotFoundInEventException {
            return UserNotFoundInEventException(
                "User $userId not found in event $eventId",
                EventErrorCode.USER_NOT_FOUND_IN_EVENT,
                mapOf(
                    "eventId" to eventId,
                    "userId" to userId,
                )
            )
        }
    }
}