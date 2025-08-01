package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.factories.EventBarReviewFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventBarReview
import ru.nascar.bms.event.repository.EventBarReviewRepository
import ru.nascar.bms.event.repository.entity.EventBarReviewEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcEventBarReviewRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
): EventBarReviewRepository {
    companion object {
        private const val SELECT = """
            select
                br.id,
                br.event_id,
                br.bar_id,
                br.score,
                br.comment,
                br.created_by,
                br.created_at,
                br.updated_by,
                br.updated_at
            from bar_reviews br
        """

        private const val SELECT_BY_EVENT_ID = """
            $SELECT
            where br.event_id = :event_id
        """

        private const val SELECT_BY_EVENT_BAR_AND_USER = """
            $SELECT
            where br.event_id = :event_id
                and br.bar_id = :bar_id
                and br.created_by = :user_id
        """


        private const val UPSERT = """
            insert into bar_reviews (id, event_id, bar_id, score, comment, created_by, created_at, updated_by, updated_at)
            values (:id, :event_id, :bar_id, :score, :comment, :created_by, :created_at, :updated_by, :updated_at)
            on conflict (event_id, bar_id, created_by) do update set
                score = :score,
                comment = :comment,
                updated_by = :updated_by,
                updated_at = :updated_at
        """

        private const val DELETE_BY_ID = """
            delete from bar_reviews
            where id = :id
        """

        private val EVENT_BAR_REVIEW_ENTITY_MAPPER = RowMapper { rs, _ ->
            EventBarReviewEntity(
                id = rs.getString("id"),
                eventId = rs.getString("event_id"),
                barId = rs.getString("bar_id"),
                score = rs.getInt("score"),
                comment = rs.getString("comment"),
                createdBy = rs.getString("created_by"),
                createdAt = rs.getUtcInstant("created_at"),
                updatedBy = rs.getString("updated_by"),
                updatedAt = rs.getUtcInstant("updated_at"),
            )
        }
    }

    override fun findAllByEventId(eventId: String): List<EventBarReview> {
        val params = mapOf(
            "event_id" to eventId
        )

        val eventBarReviewsDb = jdbcTemplate.query(
            SELECT_BY_EVENT_ID,
            params,
            EVENT_BAR_REVIEW_ENTITY_MAPPER
        )

        return eventBarReviewsDb.map { eventBarReviewDb -> createEventBarReview(eventBarReviewDb) }
    }

    override fun findByEventBarAndUser(eventId: String, barId: String, userId: String): EventBarReview? {
        val params = mapOf(
            "event_id" to eventId,
            "bar_id" to barId,
            "user_id" to userId
        )

        val eventBarReviewsDb = jdbcTemplate.query(
            SELECT_BY_EVENT_BAR_AND_USER,
            params,
            EVENT_BAR_REVIEW_ENTITY_MAPPER
        )

        return eventBarReviewsDb.map { eventBarReviewDb -> createEventBarReview(eventBarReviewDb) }.firstOrNull()
    }

    override fun saveAllFromEvent(event: Event) {
        val currentlyExistingInDb = findAllByEventId(event.id)

        val eventBarReviewsToDelete = currentlyExistingInDb.filter { eventBarReview -> eventBarReview !in event.reviews }
        val eventBarReviewsToSave = event.reviews.filter { eventBarReview -> eventBarReview !in eventBarReviewsToDelete }

        // TODO: do in batch + transaction
        eventBarReviewsToDelete.forEach { eventBarReview -> delete(eventBarReview.id) }
        eventBarReviewsToSave.forEach { eventBarReview -> save(eventBarReview) }
    }

    override fun save(eventBarReview: EventBarReview) {
        val params = mapOf(
            "id" to eventBarReview.id,
            "event_id" to eventBarReview.eventId,
            "bar_id" to eventBarReview.barId,
            "score" to eventBarReview.score,
            "comment" to eventBarReview.comment,
            "created_by" to eventBarReview.createdBy,
            "created_at" to eventBarReview.createdAt.toOffsetDateTime(),
            "updated_by" to eventBarReview.updatedBy,
            "updated_at" to eventBarReview.updatedAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params,
        )
    }

    override fun delete(eventBarReviewId: String) {
        val params = mapOf(
            "id" to eventBarReviewId
        )

        jdbcTemplate.update(
            DELETE_BY_ID,
            params,
        )
    }

    private fun createEventBarReview(eventBarReviewEntity: EventBarReviewEntity): EventBarReview {
        return EventBarReviewFactory.createFromDb(
            id = eventBarReviewEntity.id,
            eventId = eventBarReviewEntity.eventId,
            barId = eventBarReviewEntity.barId,
            score = eventBarReviewEntity.score,
            comment = eventBarReviewEntity.comment,
            createdBy = eventBarReviewEntity.createdBy,
            createdAt = eventBarReviewEntity.createdAt,
            updatedBy = eventBarReviewEntity.updatedBy,
            updatedAt = eventBarReviewEntity.updatedAt,
        )
    }
}