package ru.nascar.bms.event.service.impl

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import ru.nascar.bms.event.domain.event.ReviewAddedEvent
import ru.nascar.bms.event.service.EventPublisher

@Service
class SpringApplicationEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) : EventPublisher {

    override fun publish(event: ReviewAddedEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}