package ru.nascar.bms.receipt.service.model

import ru.nascar.bms.receipt.domain.model.Receipt

class ReceiptInternal(
    val id: String,
    val receiptData: ByteArray
) {
    companion object {
        fun fromDomain(receipt: Receipt): ReceiptInternal {
            return ReceiptInternal(
                id = receipt.id,
                receiptData = receipt.receiptData,
            )
        }
    }
}