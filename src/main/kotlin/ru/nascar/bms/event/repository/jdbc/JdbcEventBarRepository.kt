package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.factories.EventBarFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventBar
import ru.nascar.bms.event.repository.EventBarRepository
import ru.nascar.bms.event.repository.entity.EventBarEntity
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

        private const val UPSERT = """
            insert into event_bars (id, event_id, bar_id, created_by, created_at)
            values (:id, :event_id, :bar_id, :created_by, :created_at)
        """

        private const val DELETE_BY_ID = """
            delete from event_bars
            where id = :id
        """

        private val EVENT_BAR_ENTITY_MAPPER = RowMapper { rs, _ ->
            EventBarEntity(
                id = rs.getString("id"),
                eventId = rs.getString("event_id"),
                barId = rs.getString("bar_id"),
                createdBy = rs.getString("created_by"),
                createdAt = rs.getUtcInstant("created_at")
            )
        }
    }

    override fun findAllByEventId(eventId: String): List<EventBar> {
        val params = mapOf(
            "event_id" to eventId
        )

        val eventBarsDb = jdbcTemplate.query(
            SELECT_BY_EVENT_ID,
            params,
            EVENT_BAR_ENTITY_MAPPER
        )

        return eventBarsDb.map { eventBarDb -> createEventBar(eventBarDb) }
    }

    override fun saveAllFromEvent(event: Event) {
        val currentlyExistingInDb = findAllByEventId(event.id)

        val eventBarsToCreate = event.eventBars.filter { it !in currentlyExistingInDb }
        val eventBarsToDelete = currentlyExistingInDb.filter { it !in event.eventBars }

        // TODO: do in batch + transaction
        eventBarsToCreate.forEach { eventBar -> save(eventBar) }
        eventBarsToDelete.forEach { eventBar -> delete(eventBar.id) }
    }

    override fun save(eventBar: EventBar) {
        val params = mapOf(
            "id" to eventBar.id,
            "event_id" to eventBar.eventId,
            "bar_id" to eventBar.barId,
            "created_by" to eventBar.createdBy,
            "created_at" to eventBar.createdAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params,
        )
    }

    override fun delete(eventBarId: String) {
        val params = mapOf(
            "id" to eventBarId
        )

        jdbcTemplate.update(
            DELETE_BY_ID,
            params,
        )
    }

    private fun createEventBar(eventBarEntity: EventBarEntity): EventBar {
        return EventBarFactory.createFromDb(
            id = eventBarEntity.id,
            eventId = eventBarEntity.eventId,
            barId = eventBarEntity.barId,
            createdBy = eventBarEntity.createdBy,
            createdAt = eventBarEntity.createdAt,
        )
    }
}