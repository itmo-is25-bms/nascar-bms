package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.factories.EventFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventStatus
import ru.nascar.bms.event.repository.EventBarRepository
import ru.nascar.bms.event.repository.EventParticipantRepository
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.repository.entity.EventEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcEventBarRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
): EventBarRepository {
    companion object {
        private const val SELECT = """
            select
                evb.id,
                evb.event_id,
                evb.bar_id,
                evb.created_by,
                evb.created_at
            from event_bars evb
        """

        private const val SELECT_BY_EVENT_ID = """
            $SELECT
            where evb.event_id = :event_id
        """
    }
}