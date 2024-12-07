package ru.nascar.bms.bar.controller.mapping

import ru.nascar.bms.bar.domain.model.Bar
import ru.nascar.bms.presentation.abstractions.BarServiceProto.*

fun Bar.toDto(): BarDto {
    val barDtoBuilder = BarDto.newBuilder()

    barDtoBuilder
        .setId(id)
        .setName(name)
        .setAddress(address)

    val barSummaryDtoBuilder = BarSummaryDto.newBuilder()

    if (summaryTags != null) {
        barSummaryDtoBuilder
            .addAllTags(summaryTags)
    }

    if (score != null) {
        barSummaryDtoBuilder
            .setScore(score)
    }

    barDtoBuilder
        .setBarSummary(barSummaryDtoBuilder.build())

    return barDtoBuilder.build()
}
