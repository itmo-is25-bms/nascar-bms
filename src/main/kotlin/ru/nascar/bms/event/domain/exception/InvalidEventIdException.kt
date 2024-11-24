package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class InvalidEventIdException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, invalidEventId: String): InvalidEventIdException {
            return InvalidEventIdException(
                "Tried modifying event $eventId while targeting $invalidEventId",
                EventErrorCode.INVALID_EVENT_ID,
                mapOf(
                    "eventId" to eventId,
                    "invalidEventId" to invalidEventId,
                )
            )
        }
    }
}