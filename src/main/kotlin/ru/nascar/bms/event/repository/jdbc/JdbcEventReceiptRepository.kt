package ru.nascar.bms.event.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.event.domain.factories.EventReceiptFactory
import ru.nascar.bms.event.domain.model.Event
import ru.nascar.bms.event.domain.model.EventReceipt
import ru.nascar.bms.event.repository.EventReceiptRepository
import ru.nascar.bms.event.repository.entity.EventReceiptEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcEventReceiptRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : EventReceiptRepository {
    companion object {
        private const val SELECT = """
            select
                evr.id,
                evr.event_id,
                evr.bar_id,
                evr.receipt_id,
                evr.created_by,
                evr.created_at
            from event_receipts evr
        """

        private const val SELECT_BY_EVENT_ID = """
            $SELECT
            where evr.event_id = :event_id
        """

        private const val UPSERT = """
            insert into event_receipts (id, event_id, bar_id, receipt_id, created_by, created_at)
            values (:id, :event_id, :bar_id, :receipt_id, :created_by, :created_at)
        """

        private const val DELETE_BY_ID = """
            delete from event_receipts
            where id = :id
        """

        private val EVENT_RECEIPT_ENTITY_MAPPER = RowMapper { rs, _ ->
            EventReceiptEntity(
                id = rs.getString("id"),
                eventId = rs.getString("event_id"),
                barId = rs.getString("bar_id"),
                receiptId = rs.getString("receipt_id"),
                createdBy = rs.getString("created_by"),
                createdAt = rs.getUtcInstant("created_at")
            )
        }
    }

    override fun findAllByEventId(eventId: String): List<EventReceipt> {
        val params = mapOf(
            "event_id" to eventId
        )

        val eventReceiptsDb = jdbcTemplate.query(
            SELECT_BY_EVENT_ID,
            params,
            EVENT_RECEIPT_ENTITY_MAPPER
        )

        return eventReceiptsDb.map { eventReceiptDb -> createEventReceipt(eventReceiptDb) }
    }

    override fun saveAllFromEvent(event: Event) {
        val currentlyExistingInDb = findAllByEventId(event.id)

        val eventReceiptsToCreate = event.receipts.filter { receipt -> receipt !in currentlyExistingInDb }
        val eventReceiptsToDelete = currentlyExistingInDb.filter { receipt -> receipt !in event.receipts }

        // TODO: do in batch + transaction
        eventReceiptsToCreate.forEach { receipt -> save(receipt) }
        eventReceiptsToDelete.forEach { receipt -> delete(receipt.id) }
    }

    override fun save(eventReceipt: EventReceipt) {
        val params = mapOf(
            "id" to eventReceipt.id,
            "event_id" to eventReceipt.eventId,
            "bar_id" to eventReceipt.barId,
            "receipt_id" to eventReceipt.receiptId,
            "created_by" to eventReceipt.createdBy,
            "created_at" to eventReceipt.createdAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params,
        )
    }

    override fun delete(eventReceiptId: String) {
        val params = mapOf(
            "id" to eventReceiptId
        )

        jdbcTemplate.update(
            DELETE_BY_ID,
            params,
        )
    }

    private fun createEventReceipt(eventReceiptEntity: EventReceiptEntity): EventReceipt {
        return EventReceiptFactory.createFromDb(
            id = eventReceiptEntity.id,
            eventId = eventReceiptEntity.eventId,
            barId = eventReceiptEntity.barId,
            receiptId = eventReceiptEntity.receiptId,
            createdBy = eventReceiptEntity.createdBy,
            createdAt = eventReceiptEntity.createdAt,
        )
    }
}