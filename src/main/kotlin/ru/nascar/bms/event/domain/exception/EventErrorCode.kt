package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsErrorCode

enum class EventErrorCode: BmsErrorCode {
    EVENT_BAR_REVIEW_EXISTS_BY_USER;

    override val code: String = name
}
