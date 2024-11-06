package ru.nascar.bms.bars

import ru.nascar.bms.bars.contracts.BarInternal

interface BarService {
    fun create(userId: String, name: String, address: String): BarInternal
    fun getById(id: String): BarInternal
    fun getAll(): List<BarInternal>
}