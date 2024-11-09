package ru.nascar.bms.bar.service.impl

import org.springframework.stereotype.Service
import ru.nascar.bms.bar.repository.entity.BarEntity
import ru.nascar.bms.bar.domain.model.Bar
import ru.nascar.bms.bar.service.mapping.toDomainModel
import ru.nascar.bms.bar.repository.BarRepository
import ru.nascar.bms.bar.service.BarService
import java.time.Clock
import java.time.OffsetDateTime
import java.util.UUID

@Service
class DefaultBarService(
    private val barRepository: BarRepository,
    private val clock: Clock,
) : BarService {
    override fun create(userId: String, name: String, address: String): Bar {
        val barId = UUID.randomUUID().toString()
        val now = OffsetDateTime.now(clock)
        val barEntity = BarEntity(
            id = barId,
            name = name,
            address = address,
            createdBy = userId,
            createdAt = now
        )

        barRepository.save(barEntity)

        return barEntity.toDomainModel()
    }

    override fun getById(id: String): Bar {
        return barRepository.getReferenceById(id).toDomainModel()
    }

    override fun getAll(): List<Bar> {
        val bars = barRepository.findAll()
        return bars.map { it.toDomainModel() }
    }
}
