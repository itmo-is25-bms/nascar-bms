package ru.nascar.bms.receipt.domain.factories

import ru.nascar.bms.receipt.domain.model.Receipt
import java.time.Instant
import java.util.UUID

class ReceiptFactory {
    companion object {
        fun createNew(receiptData: ByteArray, createdBy: String, createdAt: Instant): Receipt {
            val id = "receipt-" + UUID.randomUUID().toString()
            return Receipt(
                id = id,
                receiptData = receiptData,
                createdBy = createdBy,
                createdAt = createdAt,
            )
        }

        fun createFromDb(id: String, receiptData: ByteArray, createdBy: String, createdAt: Instant): Receipt {
            return Receipt(
                id = id,
                receiptData = receiptData,
                createdBy = createdBy,
                createdAt = createdAt
            )
        }
    }
}