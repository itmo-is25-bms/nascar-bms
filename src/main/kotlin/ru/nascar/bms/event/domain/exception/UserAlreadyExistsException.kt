package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class UserAlreadyExistsException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, userId: String): UserAlreadyExistsException {
            return UserAlreadyExistsException(
                "User $userId in event $eventId already exists",
                EventErrorCode.EVENT_PARTICIPANT_EXISTS,
                mapOf(
                    "eventId" to eventId,
                    "userId" to userId,
                )
            )
        }
    }
}