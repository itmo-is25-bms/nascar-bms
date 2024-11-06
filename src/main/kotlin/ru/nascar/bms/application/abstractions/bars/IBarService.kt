package ru.nascar.bms.application.abstractions.bars

import ru.nascar.bms.application.abstractions.bars.contracts.BarInternal

interface IBarService {
    fun create(userId: String, name: String, address: String)
    fun getById(id: String): BarInternal
    fun getAll(): List<BarInternal>
}