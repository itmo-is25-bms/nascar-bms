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
            val errorCode = statusToErrorCode(eventStatus)
            return InvalidEventStatusException(
                "Can't execute action $actionName on event $eventId while in status $eventStatus",
                errorCode,
                mapOf(
                    "eventId" to eventId,
                    "eventStatus" to eventStatus.name,
                    "actionName" to actionName,
                )
            )
        }

        private fun statusToErrorCode(eventStatus: EventStatus): EventErrorCode {
            return when(eventStatus) {
                EventStatus.CREATED -> EventErrorCode.INVALID_EVENT_STATUS_CREATED
                EventStatus.IN_PROGRESS -> EventErrorCode.INVALID_EVENT_STATUS_IN_PROGRESS
                EventStatus.FINISHED -> EventErrorCode.INVALID_EVENT_STATUS_FINISHED
            }
        }
    }
}