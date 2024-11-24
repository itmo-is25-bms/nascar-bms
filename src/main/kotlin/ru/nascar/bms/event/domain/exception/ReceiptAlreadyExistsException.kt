package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class ReceiptAlreadyExistsException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun create(eventId: String, barId: String): ReceiptAlreadyExistsException {
            return ReceiptAlreadyExistsException(
                "Receipt for bar $barId during event $eventId already exists",
                EventErrorCode.EVENT_BAR_RECEIPT_EXISTS,
                mapOf(
                    "eventId" to eventId,
                    "barId" to barId,
                )
            )
        }
    }
}