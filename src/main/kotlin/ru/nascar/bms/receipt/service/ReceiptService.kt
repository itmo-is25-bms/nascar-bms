package ru.nascar.bms.receipt.service

import ru.nascar.bms.receipt.service.model.ReceiptInternal

interface ReceiptService {
    fun create(receiptData: ByteArray, createdBy: String): ReceiptInternal
}