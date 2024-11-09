package ru.nascar.bms.bar.controller.mapping

import ru.nascar.bms.bar.domain.model.Bar
import ru.nascar.bms.presentation.abstractions.BarServiceProto.BarDto

fun Bar.toDto(): BarDto = BarDto.newBuilder()
    .setId(id)
    .setName(name)
    .setAddress(address)
    .build()
