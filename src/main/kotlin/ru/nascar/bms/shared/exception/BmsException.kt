package ru.nascar.bms.shared.exception

open class BmsException(
    message: String,
    val errorCode: BmsErrorCode,
    val errorData: Map<String, String> = emptyMap()
): RuntimeException(message)
