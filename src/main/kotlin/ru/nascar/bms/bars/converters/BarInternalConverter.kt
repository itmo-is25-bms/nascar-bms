package ru.nascar.bms.bars.converters

import ru.nascar.bms.bars.contracts.BarInternal
import ru.nascar.bms.bars.contracts.Bar

class BarInternalConverter {
    companion object {
        fun fromDomain(bar: Bar): BarInternal = BarInternal(id = bar.id, name = bar.name, address = bar.address)
    }
}