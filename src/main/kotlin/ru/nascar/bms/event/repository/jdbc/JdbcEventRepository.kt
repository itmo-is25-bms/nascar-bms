package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.factories.EventFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.repository.entity.EventEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcEventRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
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

        private const val UPSERT = """
            BEGIN;

            insert into events (id, name, status, passcode, start_datetime, created_by, created_at, updated_by, updated_at)
            values (:id, :name, :status, :passcode, :start_datetime, :user, :now, :user, :now)
            on conflict (id) do update set
                name = :name,
                status = :status,
                start_datetime = :start_datetime,
                updated_by = :user,
                updated_at = :now;
                
            COMMIT;
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
                updatedBy = rs.getString("updated_at"),
                updatedAt = rs.getUtcInstant("updated_at"),
            )
        }

        private val EVENT_MAPPER = RowMapper { rs, _ ->
            EventFactory.createFromDb(
                id = rs.getString("id"),
                name = rs.getString("name"),
                status = rs.getString("status"),
                passcode = rs.getString("passcode"),
                startDateTime = rs.getUtcInstant("start_datetime"),
                createdBy = rs.getString("created_by"),
                createdAt = rs.getUtcInstant("created_at"),
                updatedBy = rs.getString("updated_at"),
                updatedAt = rs.getUtcInstant("updated_at"),
            )
        }
    }

    override fun findAllByCreatedBy(createdBy: String): List<Event> {
        val params = mapOf(
            "createdBy" to createdBy
        )

        return jdbcTemplate.query(
            SELECT_BY_CREATED_BY,
            params,
            EVENT_ENTITY_MAPPER
        )
    }

    override fun findByPasscode(passcode: String): Event? {
        val params = mapOf(
            "passcode" to passcode
        )

        return jdbcTemplate.query(
            SELECT_BY_PASSCODE,
            params,
            EVENT_ENTITY_MAPPER
        ).firstOrNull()
    }

    override fun findByPasscode(passcode: String): Event? {
        val params = mapOf(
            "passcode" to passcode
        )

        return jdbcTemplate.query(
            SELECT_BY_PASSCODE,
            params,
            EVENT_ENTITY_MAPPER
        ).firstOrNull()
    }

    override fun save(event: Event) {
        // TODO: take user from context
        val params = mapOf(
            "id" to event.id,
            "name" to event.name,
            "status" to event.status,
            "passcode" to event.passcode,
            "start_datetime" to event.startDateTime.toOffsetDateTime(),
            "user" to event.updatedBy,
            "now" to event.updatedAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params
        )
    }
}