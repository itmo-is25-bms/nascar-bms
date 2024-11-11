package ru.nascar.bms.receipt.repository

import ru.nascar.bms.receipt.domain.model.Receipt

interface ReceiptRepository {
    fun save(receipt: Receipt)
}