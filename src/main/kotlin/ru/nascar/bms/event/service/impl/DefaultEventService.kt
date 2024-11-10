package ru.nascar.bms.event.service.impl

import org.springframework.stereotype.Service
import ru.nascar.bms.event.domain.factories.EventFactory
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.service.EventService
import ru.nascar.bms.event.service.model.EventInternal
import java.time.Clock
import java.time.Instant
import java.util.UUID

@Service
class DefaultEventService(
    private val eventRepository: EventRepository,
    private val clock: Clock,
) : EventService {
    override fun create(userId: String, name: String, startDatetime: Instant, eventBars: List<String>): EventInternal {
        val passcode = UUID.randomUUID().toString()
        val event = EventFactory.createNew(
            name = name,
            passcode = passcode,
            startDateTime = startDatetime,
            eventBars = eventBars,
            createdBy = userId,
            createdAt = clock.instant(),
        )

        eventRepository.save(event)

        return EventInternal.fromDomain(event)
    }

    override fun getById(id: String): EventInternal {
        val event = eventRepository.findById(id)
        return EventInternal.fromDomain(event!!)
    }

    override fun getByPasscode(passcode: String): EventInternal? {
        val event = eventRepository.findByPasscode(passcode)

        return when (event) {
            null -> null
            else -> EventInternal.fromDomain(event)
        }
    }

    override fun addUserToEvent(id: String, userId: String) {
        val event = eventRepository.findById(id)!!

        event.addUser(userId)

        eventRepository.save(event)
    }
}