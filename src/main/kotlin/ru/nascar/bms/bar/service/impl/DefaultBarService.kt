package ru.nascar.bms.bar.service.impl

import org.springframework.dao.DuplicateKeyException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.nascar.bms.bar.domain.exception.BarAlreadyExistsException
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

    @Retryable(
        include = [DuplicateKeyException::class],
        backoff = Backoff(random = true, delay = 50, maxDelay = 200)
    )
    @Transactional
    override fun create(userId: String, name: String, address: String): Bar {
        validateNoBarWithNameAndAddressExists(name, address)

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

    private fun validateNoBarWithNameAndAddressExists(name: String, address: String) {
        val existingBar = barRepository.findByNameAndAddress(name, address)

        if (existingBar != null) {
            throw BarAlreadyExistsException.withNameAndAddress(name, address)
        }
    }

    override fun getById(id: String): Bar {
        return findById(id) ?: throw BarNotFoundException.withBarId(id)
    }

    private fun findById(id: String): Bar? {
        return findByIds(setOf(id)).firstOrNull()
    }

    override fun findByIds(barIds: Set<String>): List<Bar> {
        return barRepository.findByIds(barIds).map { it.toDomainModel() }
    }

    override fun getAll(): List<Bar> {
        val bars = barRepository.findAll()
        return bars.map { it.toDomainModel() }
    }
}
