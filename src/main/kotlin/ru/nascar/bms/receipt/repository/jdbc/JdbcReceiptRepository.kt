package ru.nascar.bms.receipt.repository.jdbc

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.infra.toOffsetDateTime
import ru.nascar.bms.receipt.domain.model.Receipt
import ru.nascar.bms.receipt.repository.ReceiptRepository

@Repository
class JdbcReceiptRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : ReceiptRepository {
    companion object {
        private const val UPSERT = """
            insert into receipts (id, receipt_data, created_by, created_at)
            values (:id, :receipt_data, :created_by, :created_at)
        """
    }
    override fun save(receipt: Receipt) {
        val params = mapOf(
            "id" to receipt.id,
            "receipt_data" to receipt.receiptData,
            "created_by" to receipt.createdBy,
            "created_at" to receipt.createdAt.toOffsetDateTime()
        )

        jdbcTemplate.update(
            UPSERT,
            params
        )
    }
}