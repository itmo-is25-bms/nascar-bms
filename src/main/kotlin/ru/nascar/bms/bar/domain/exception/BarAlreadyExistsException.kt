package ru.nascar.bms.bar.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class BarAlreadyExistsException(
    message: String,
    errorCode: BarErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun withNameAndAddress(name: String, address: String): BarAlreadyExistsException {
            return BarAlreadyExistsException(
                "Bar with name $name and address $address already exists",
                BarErrorCode.BAR_WITH_NAME_AND_ADDRESS_ALREADY_EXISTS,
                mapOf(
                    "name" to name,
                    "address" to address
                )
            )
        }
    }
}
