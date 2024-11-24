package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class EventBarsNotFoundException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData)  {
    companion object {
        fun forBarIds(barIds: Collection<String>): EventBarsNotFoundException {
            val sortedIds = barIds.sorted()

            return EventBarsNotFoundException(
                "Bars with ids $sortedIds were not found",
                EventErrorCode.EVENT_BARS_NOT_FOUND_BY_IDS,
                mapOf("barIds" to sortedIds.toString())
            )
        }
    }
}
