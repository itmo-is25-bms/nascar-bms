package ru.nascar.bms.bar.service

import ru.nascar.bms.bar.domain.model.Bar

interface BarService {
    fun create(userId: String, name: String, address: String): Bar
    fun getById(id: String): Bar
    fun findByIds(barIds: Set<String>): List<Bar>
    fun getAll(): List<Bar>
}
