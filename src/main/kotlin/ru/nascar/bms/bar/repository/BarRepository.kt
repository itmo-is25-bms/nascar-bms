package ru.nascar.bms.bar.repository

import ru.nascar.bms.bar.repository.entity.BarEntity

interface BarRepository {
    fun findByIds(ids: Collection<String>): List<BarEntity>
    fun findByNameAndAddress(name: String, address: String): BarEntity?
    fun findAll(): List<BarEntity>
    fun save(bar: BarEntity)
}
