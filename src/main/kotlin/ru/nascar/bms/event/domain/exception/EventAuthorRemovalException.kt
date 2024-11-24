package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class EventAuthorRemovalException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun forEventAndAuthor(eventId: String, eventAuthorId: String): EventAuthorRemovalException {
            return EventAuthorRemovalException(
                "Can't remove author $eventAuthorId of event $eventId",
                EventErrorCode.EVENT_AUTHOR_REMOVAL,
                mapOf(
                    "eventId" to eventId,
                    "eventAuthorId" to eventAuthorId
                )
            )
        }
    }
}
