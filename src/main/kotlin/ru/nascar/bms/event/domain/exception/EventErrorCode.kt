package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsErrorCode

enum class EventErrorCode : BmsErrorCode {
    EVENT_BAR_REVIEW_EXISTS_BY_USER,
    INVALID_EVENT_ID,
    BAR_NOT_FOUND_IN_EVENT,
    USER_NOT_FOUND_IN_EVENT,
    EVENT_BAR_RECEIPT_EXISTS,
    UNAUTHORIZED_EVENT_ACTION,
    EVENT_PARTICIPANT_EXISTS,
    INVALID_EVENT_STATUS,
    ;

    override val code: String = name
}
