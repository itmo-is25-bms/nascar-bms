package ru.nascar.bms.shared.exception

interface BmsErrorCode {
    companion object {
        const val UNKNOWN = "UNKNOWN"
    }

    val code: String
}
