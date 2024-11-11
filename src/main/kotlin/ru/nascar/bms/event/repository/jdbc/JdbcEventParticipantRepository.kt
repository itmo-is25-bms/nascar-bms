package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.factories.EventParticipantFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventParticipant
import ru.nascar.bms.event.repository.EventParticipantRepository
import ru.nascar.bms.event.repository.entity.EventParticipantEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcEventParticipantRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : EventParticipantRepository {
    companion object {
        private const val SELECT = """
            select
                evp.id,
                evp.event_id,
                evp.user_id,
                evp.joined_at,
                evp.created_by,
                evp.created_at
            from event_participants evp
        """

        private const val SELECT_BY_EVENT_ID = """
            $SELECT
            where evp.event_id = :event_id
        """

        private const val UPSERT = """
            insert into event_participants (id, event_id, user_id, joined_at, created_by, created_at)
            values (:id, :event_id, :user_id, :joined_at, :created_by, :created_at)
        """

        private const val DELETE_BY_ID = """
            delete from event_participants
            where id = :id
        """

        private val EVENT_PARTICIPANT_ENTITY_MAPPER = RowMapper { rs, _ ->
            EventParticipantEntity(
                id = rs.getString("id"),
                eventId = rs.getString("event_id"),
                userId = rs.getString("user_id"),
                joinedAt = rs.getUtcInstant("joined_at"),
                createdBy = rs.getString("created_by"),
                createdAt = rs.getUtcInstant("created_at"),
            )
        }
    }

    override fun findAllByEventId(eventId: String): List<EventParticipant> {
        val params = mapOf(
            "event_id" to eventId
        )

        val eventParticipantsDb = jdbcTemplate.query(
            SELECT_BY_EVENT_ID,
            params,
            EVENT_PARTICIPANT_ENTITY_MAPPER
        )

        return eventParticipantsDb.map { eventParticipantDb -> createEventParticipant(eventParticipantDb) }
    }

    override fun saveAllFromEvent(event: Event) {
        val currentlyExistingInDb = findAllByEventId(event.id)

        val eventParticipantsToCreate = event.participants.filter { eventParticipant -> eventParticipant !in currentlyExistingInDb }
        val eventParticipantsToDelete = currentlyExistingInDb.filter { eventParticipant -> eventParticipant !in event.participants }

        // TODO: do in batch + transaction
        eventParticipantsToCreate.forEach { eventParticipant -> save(eventParticipant) }
        eventParticipantsToDelete.forEach { eventParticipant -> delete(eventParticipant.id) }
    }

    override fun save(eventParticipant: EventParticipant) {
        val params = mapOf(
            "id" to eventParticipant.id,
            "event_id" to eventParticipant.eventId,
            "user_id" to eventParticipant.userId,
            "joined_at" to eventParticipant.joinedAt.toOffsetDateTime(),
            "created_by" to eventParticipant.createdBy,
            "created_at" to eventParticipant.createdAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params,
        )
    }

    override fun delete(eventParticipantId: String) {
        val params = mapOf(
            "id" to eventParticipantId
        )

        jdbcTemplate.update(
            DELETE_BY_ID,
            params,
        )
    }

    private fun createEventParticipant(eventParticipantEntity: EventParticipantEntity): EventParticipant {
        return EventParticipantFactory.createFromDb(
            id = eventParticipantEntity.id,
            eventId = eventParticipantEntity.eventId,
            userId = eventParticipantEntity.userId,
            joinedAt = eventParticipantEntity.joinedAt,
            createdBy = eventParticipantEntity.createdBy,
            createdAt = eventParticipantEntity.createdAt,
        )
    }
}