package ru.nascar.bms.event.service.impl

import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.service.EventService

class DefaultEventService(
    private val eventRepository: EventRepository,
) : EventService {
}