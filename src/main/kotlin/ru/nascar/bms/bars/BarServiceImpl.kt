package ru.nascar.bms.bars

import org.springframework.stereotype.Component
import ru.nascar.bms.bars.contracts.Bar
import ru.nascar.bms.bars.contracts.BarInternal
import ru.nascar.bms.bars.converters.BarInternalConverter

@Component
class BarServiceImpl(
    private val barRepository: BarRepository,
) : BarService {
    override fun create(userId: String, name: String, address: String): BarInternal {
        val bar = Bar.createNew(name = name, address = address, userId = userId)

        barRepository.save(bar)

        return BarInternalConverter.fromDomain(bar)
    }

    override fun getById(id: String): BarInternal {
        val bar = barRepository.findById(id).get()

        return BarInternalConverter.fromDomain(bar)
    }

    override fun getAll(): List<BarInternal> {
        val bars = barRepository.findAll()

        return bars.map { bar -> BarInternalConverter.fromDomain(bar) }
    }
}