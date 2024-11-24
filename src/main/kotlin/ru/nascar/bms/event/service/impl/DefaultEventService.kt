package ru.nascar.bms.event.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.nascar.bms.bar.service.BarService
import ru.nascar.bms.event.domain.exception.EventBarsNotFoundException
import ru.nascar.bms.event.domain.factories.EventFactory
import ru.nascar.bms.event.domain.factories.EventParticipantFactory
import ru.nascar.bms.event.repository.EventParticipantRepository
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.service.EventService
import ru.nascar.bms.event.service.model.EventInternal
import java.time.Clock
import java.time.Instant
import java.util.UUID

@Service
class DefaultEventService(
    private val barService: BarService,
    private val eventRepository: EventRepository,
    private val eventParticipantRepository: EventParticipantRepository,
    private val clock: Clock,
) : EventService {
    @Transactional
    override fun create(userId: String, name: String, startDatetime: Instant, eventBarsIds: Set<String>): EventInternal {
        validateAllBarsExist(eventBarsIds)

        val passcode = UUID.randomUUID().toString()
        val event = EventFactory.createNew(
            name = name,
            passcode = passcode,
            startDateTime = startDatetime,
            eventBarsIds = eventBarsIds,
            createdBy = userId,
            createdAt = clock.instant(),
        )

        eventRepository.save(event)

        return EventInternal.fromDomain(event)
    }

    private fun validateAllBarsExist(barIds: Set<String>) {
        val existentBarIds = barService.findByIds(barIds).mapTo(HashSet()) { it.id }
        val nonExistentBarIds = barIds - existentBarIds

        if (nonExistentBarIds.isNotEmpty()) {
            throw EventBarsNotFoundException.forBarIds(nonExistentBarIds)
        }
    }

    override fun getById(id: String): EventInternal {
        val event = eventRepository.getById(id)
        return EventInternal.fromDomain(event)
    }

    override fun findByPasscode(passcode: String): EventInternal? {
        val event = eventRepository.findByPasscode(passcode)
        return if (event == null) null else EventInternal.fromDomain(event)
    }

    override fun getByUserId(userId: String): List<EventInternal> {
        val eventIds = eventParticipantRepository.findAllByUserId(userId).map { it.eventId }
        val events = eventRepository.findByIds(eventIds)
        return events.map { event -> EventInternal.fromDomain(event) }
    }

    @Transactional
    override fun addUserToEvent(id: String, userId: String) {
        val event = eventRepository.getById(id)
        val user = EventParticipantFactory.createNew(
            eventId = id,
            userId = userId,
            createdAt = clock.instant(),
            createdBy = userId,
        )

        event.addUser(user)

        eventRepository.save(event)
    }

    @Transactional
    override fun removeUserFromEvent(id: String, userId: String) {
        val event = eventRepository.getById(id)
        val user = EventParticipantFactory.createNew(
            eventId = id,
            userId = userId,
            createdAt = clock.instant(),
            createdBy = userId,
        )

        event.removeUser(user)

        eventRepository.save(event)
    }
}