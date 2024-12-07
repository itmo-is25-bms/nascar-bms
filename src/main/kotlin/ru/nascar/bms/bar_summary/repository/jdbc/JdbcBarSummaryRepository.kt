package ru.nascar.bms.bar_summary.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.bar_summary.domain.BarSummary
import ru.nascar.bms.bar_summary.domain.ReviewEntry
import ru.nascar.bms.bar_summary.repository.BarSummaryRepository
import ru.nascar.bms.bar_summary.repository.entity.ReviewEntryEntity
import ru.nascar.bms.infra.getUtcInstant

@Repository
class JdbcBarSummaryRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    ) : BarSummaryRepository {
    companion object {
        private const val SELECT_REVIEWS_BY_BAR_ID = """
            select
                br.score,
                br.comment,
                br.updated_at
            from bar_reviews br
            where br.bar_id = :bar_id
        """

        private const val UPSERT_BAR_SUMMARY = """
            update bars
            set 
                summary_tags = :summary_tags,
                score = :score
            where id = :bar_id
        """

        private val REVIEW_ENTRY_ENTITY_MAPPER = RowMapper { rs, _ ->
            ReviewEntryEntity(
                score = rs.getInt("score"),
                comment = if (rs.getObject("comment") != null) rs.getString("comment") else null,
                timestamp = rs.getUtcInstant("updated_at"),
            )
        }
    }

    override fun getReviewsForBar(barId: String): List<ReviewEntry> {
        val params = mapOf(
            "bar_id" to barId,
        )

        val reviewEntryEntities = jdbcTemplate.query(
            SELECT_REVIEWS_BY_BAR_ID,
            params,
            REVIEW_ENTRY_ENTITY_MAPPER
        )

        return reviewEntryEntities.map { reviewEntryDb -> createReviewEntry(reviewEntryDb) }
    }

    override fun updateBarSummary(barId: String, summary: BarSummary) {
        val params = mapOf(
            "bar_id" to barId,
            "summary_tags" to summary.summaryTags.toTypedArray(),
            "score" to summary.score,
        )

        jdbcTemplate.update(
            UPSERT_BAR_SUMMARY,
            params
        )
    }

    private fun createReviewEntry(reviewEntryDb: ReviewEntryEntity): ReviewEntry {
        return ReviewEntry(
            score = reviewEntryDb.score,
            comment = reviewEntryDb.comment,
            timestamp = reviewEntryDb.timestamp,
        )
    }
}