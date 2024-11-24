package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class BarNotFoundInEventException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, barId: String): BarNotFoundInEventException {
            return BarNotFoundInEventException(
                "Bar $barId not found in event $eventId",
                EventErrorCode.BAR_NOT_FOUND_IN_EVENT,
                mapOf(
                    "eventId" to eventId,
                    "barId" to barId,
                )
            )
        }
    }
}