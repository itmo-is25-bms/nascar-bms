package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.repository.entity.EventEntity
import ru.nascar.bms.infra.getUtcInstant

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
    }

    override fun findAllByCreatedBy(createdBy: String): List<EventEntity> {
        val params = mapOf(
            "createdBy" to createdBy
        )

        return jdbcTemplate.query(
            SELECT_BY_CREATED_BY,
            params,
            EVENT_ENTITY_MAPPER
        )
    }

    override fun findByPasscode(passcode: String): EventEntity? {
        val params = mapOf(
            "passcode" to passcode
        )

        return jdbcTemplate.query(
            SELECT_BY_PASSCODE,
            params,
            EVENT_ENTITY_MAPPER
        ).firstOrNull()
    }
}