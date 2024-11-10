package ru.nascar.bms.bar.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class BarNotFoundException(
    message: String,
    errorCode: BarErrorCode,
    errorData: Map<String, String> = emptyMap()
) : BmsException(message, errorCode, errorData) {
    companion object {
        fun withBarId(barId: String): BarNotFoundException {
            return BarNotFoundException(
                "Bar with id $barId not found",
                BarErrorCode.BAR_NOT_FOUND_BY_ID,
                mapOf("barId" to barId)
            )
        }
    }
}
