package ru.nascar.bms.utils

import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import ru.nascar.bms.event.domain.event.ReviewAddedEvent
import ru.nascar.bms.event.service.EventPublisher

class EventAssertions(
    private val eventPublisherMock: () -> EventPublisher,
) {

    fun assertEventsSent(event: ReviewAddedEvent) {
        verify(eventPublisherMock()).publish(event)
    }

    fun assertNoEventsSent() {
        verify(eventPublisherMock(), never()).publish(any())
    }
}