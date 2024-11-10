package ru.nascar.bms.bar.domain.exception

import ru.nascar.bms.shared.exception.BmsErrorCode

enum class BarErrorCode: BmsErrorCode {
    BAR_NOT_FOUND_BY_ID;

    override val code: String = name
}