package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.exception.EventNotFoundException
import ru.nascar.bms.event.domain.factories.EventFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventStatus
import ru.nascar.bms.event.repository.EventBarRepository
import ru.nascar.bms.event.repository.EventBarReviewRepository
import ru.nascar.bms.event.repository.EventParticipantRepository
import ru.nascar.bms.event.repository.EventReceiptRepository
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.repository.entity.EventEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcEventRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val eventBarRepository: EventBarRepository,
    private val eventParticipantRepository: EventParticipantRepository,
    private val eventReceiptRepository: EventReceiptRepository,
    private val eventBarReviewRepository: EventBarReviewRepository,
) : EventRepository {

    companion object {
        private const val SELECT = """
            select 
                ev.id,
                ev.name,
                ev.status,
                ev.passcode,
                ev.start_datetime,
                ev.created_by,
                ev.created_at,
                ev.updated_by,
                ev.updated_at
            from events ev
        """

        private const val SELECT_BY_CREATED_BY = """
            $SELECT
            where ev.created_by = :createdBy
        """

        private const val SELECT_BY_PASSCODE = """
            $SELECT
            where ev.passcode = :passcode
        """

        private const val SELECT_BY_ID = """
            $SELECT
            where ev.id in (:ids)
        """

        private const val UPSERT = """
            insert into events (id, name, status, passcode, start_datetime, created_by, created_at, updated_by, updated_at)
            values (:id, :name, :status, :passcode, :start_datetime, :user, :now, :user, :now)
            on conflict (id) do update set
                name = :name,
                status = :status,
                start_datetime = :start_datetime,
                updated_by = :user,
                updated_at = :now
        """

        private val EVENT_ENTITY_MAPPER = RowMapper { rs, _ ->
            EventEntity(
                id = rs.getString("id"),
                name = rs.getString("name"),
                status = rs.getString("status"),
                passcode = rs.getString("passcode"),
                startDateTime = rs.getUtcInstant("start_datetime"),
                createdBy = rs.getString("created_by"),
                createdAt = rs.getUtcInstant("created_at"),
                updatedBy = rs.getString("updated_by"),
                updatedAt = rs.getUtcInstant("updated_at"),
            )
        }
    }

    override fun findAllByCreatedBy(createdBy: String): List<Event> {
        val params = mapOf(
            "createdBy" to createdBy
        )

        val eventsDb = jdbcTemplate.query(
            SELECT_BY_CREATED_BY,
            params,
            EVENT_ENTITY_MAPPER
        )

        return eventsDb.map { eventDb -> createEvent(eventDb) }
    }

    override fun findByPasscode(passcode: String): Event? {
        val params = mapOf(
            "passcode" to passcode
        )

        val eventDb = jdbcTemplate.query(
            SELECT_BY_PASSCODE,
            params,
            EVENT_ENTITY_MAPPER
        ).firstOrNull()

        return if(eventDb == null) null else createEvent(eventDb)
    }

    override fun findById(id: String): Event? {
        return findByIds(listOf(id)).firstOrNull()
    }

    override fun getById(id: String): Event {
        return findById(id) ?: throw EventNotFoundException.create(eventId = id)
    }

    override fun findByIds(ids: Collection<String>): List<Event> {
        if (ids.isEmpty()) {
            return emptyList()
        }

        val eventsDb = jdbcTemplate.query(
            SELECT_BY_ID,
            mapOf("ids" to ids),
            EVENT_ENTITY_MAPPER
        )

        return eventsDb.map { createEvent(it) }
    }

    override fun save(event: Event) {
        // TODO: take user from context
        val params = mapOf(
            "id" to event.id,
            "name" to event.name,
            "status" to event.status.toString(),
            "passcode" to event.passcode,
            "start_datetime" to event.startDateTime.toOffsetDateTime(),
            "user" to event.updatedBy,
            "now" to event.updatedAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params
        )

        // TODO: In transaction
        eventBarRepository.saveAllFromEvent(event)
        eventParticipantRepository.saveAllFromEvent(event)
        eventReceiptRepository.saveAllFromEvent(event)
        eventBarReviewRepository.saveAllFromEvent(event)
    }

    private fun createEvent(eventEntity: EventEntity): Event {
        val eventBars = eventBarRepository.findAllByEventId(eventEntity.id)
        val eventParticipants = eventParticipantRepository.findAllByEventId(eventEntity.id)
        val eventReceipts = eventReceiptRepository.findAllByEventId(eventEntity.id)
        val eventBarReviews = eventBarReviewRepository.findAllByEventId(eventEntity.id)

        return EventFactory.createFromDb(
            id = eventEntity.id,
            name = eventEntity.name,
            status = EventStatus.valueOf(eventEntity.status),
            passcode = eventEntity.passcode,
            startDateTime = eventEntity.startDateTime,
            eventBars = eventBars,
            participants = eventParticipants,
            receipts = eventReceipts,
            reviews = eventBarReviews,
            createdBy = eventEntity.createdBy,
            createdAt = eventEntity.createdAt,
            updatedBy = eventEntity.updatedBy,
            updatedAt = eventEntity.updatedAt,
        )
    }
}