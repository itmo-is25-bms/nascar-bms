package ru.nascar.bms.bars.converters

import ru.nascar.bms.bars.contracts.BarInternal
import ru.nascar.bms.presentation.abstractions.BarServiceProto.BarDto

class BarDtoConverter {
    companion object {
        fun fromInternal(barInternal: BarInternal): BarDto = BarDto.newBuilder()
            .setId(barInternal.id)
            .setName(barInternal.name)
            .setAddress(barInternal.address)
            .build()
    }
}