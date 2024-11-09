package ru.nascar.bms.bar.service.mapping

import ru.nascar.bms.bar.domain.model.Bar
import ru.nascar.bms.bar.repository.entity.BarEntity

fun BarEntity.toDomainModel(): Bar {
    return Bar(
        id = id,
        name = name,
        address = address
    )
}
