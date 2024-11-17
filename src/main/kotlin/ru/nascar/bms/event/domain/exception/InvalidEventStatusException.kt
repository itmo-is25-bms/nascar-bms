package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.event.domain.model.EventStatus
import ru.nascar.bms.shared.exception.BmsException

class InvalidEventStatusException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, eventStatus: EventStatus, actionName: String): InvalidEventStatusException {
            return InvalidEventStatusException(
                "Can't execute action $actionName on event $eventId while in status $eventStatus",
                EventErrorCode.INVALID_EVENT_STATUS,
                mapOf(
                    "eventId" to eventId,
                    "eventStatus" to eventStatus.toString(),
                    "actionName" to actionName,
                )
            )
        }
    }
}