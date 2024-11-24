package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class EventNotFoundException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String): EventNotFoundException {
            return EventNotFoundException(
                "Event $eventId not found",
                EventErrorCode.EVENT_NOT_FOUND,
                mapOf(
                    "eventId" to eventId,
                )
            )
        }
    }
}