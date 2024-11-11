package ru.nascar.bms.bar.service.impl

import org.springframework.stereotype.Service
import ru.nascar.bms.bar.domain.exception.BarNotFoundException
import ru.nascar.bms.bar.domain.model.Bar
import ru.nascar.bms.bar.repository.BarRepository
import ru.nascar.bms.bar.repository.entity.BarEntity
import ru.nascar.bms.bar.service.BarService
import ru.nascar.bms.bar.service.mapping.toDomainModel
import java.time.Clock
import java.util.UUID

@Service
class DefaultBarService(
    private val barRepository: BarRepository,
    private val clock: Clock,
) : BarService {
    override fun create(userId: String, name: String, address: String): Bar {
        val barId = "bar-" + UUID.randomUUID().toString()
        val barEntity = BarEntity(
            id = barId,
            name = name,
            address = address,
            createdBy = userId,
            createdAt = clock.instant(),
        )

        barRepository.save(barEntity)

        return barEntity.toDomainModel()
    }

    override fun getById(id: String): Bar {
        return findById(id) ?: throw BarNotFoundException.withBarId(id)
    }

    private fun findById(id: String): Bar? {
        return barRepository.findById(id)?.toDomainModel()
    }

    override fun getAll(): List<Bar> {
        val bars = barRepository.findAll()
        return bars.map { it.toDomainModel() }
    }
}
