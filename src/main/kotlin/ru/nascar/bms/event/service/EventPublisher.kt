package ru.nascar.bms.event.service

import ru.nascar.bms.event.domain.event.ReviewAddedEvent

interface EventPublisher {

    fun publish(event: ReviewAddedEvent)
}