package ru.nascar.bms.event.service.model

import ru.nascar.bms.event.domain.model.EventStatus

enum class EventStatusInternal {
    CREATED, IN_PROGRESS, FINISHED, ;

    companion object {
        fun fromDomain(status: EventStatus): EventStatusInternal {
            return when (status) {
                EventStatus.CREATED -> CREATED
                EventStatus.IN_PROGRESS -> IN_PROGRESS
                EventStatus.FINISHED -> FINISHED
            }
        }
    }
}