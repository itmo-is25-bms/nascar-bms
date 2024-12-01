package ru.nascar.bms.event.controller.mapping

import com.google.protobuf.Timestamp
import ru.nascar.bms.event.service.model.EventBarInternal
import ru.nascar.bms.event.service.model.EventBarReviewInternal
import ru.nascar.bms.event.service.model.EventInternal
import ru.nascar.bms.event.service.model.EventStatusInternal
import ru.nascar.bms.presentation.abstractions.EventServiceProto.EventBarDto
import ru.nascar.bms.presentation.abstractions.EventServiceProto.EventBarReviewDto
import ru.nascar.bms.presentation.abstractions.EventServiceProto.EventDto
import ru.nascar.bms.presentation.abstractions.EventServiceProto.EventStatusDto
import java.time.Instant

fun EventInternal.toDto(): EventDto = EventDto.newBuilder()
    .setId(id)
    .setName(name)
    .setStatus(status.toDto())
    .setPasscode(passcode)
    .setStartDate(startDateTime.toTimestamp())
    .setAuthorUserId(createdBy)
    .addAllParticipantIds(participants)
    .addAllBarIds(eventBars.map { bar -> bar.id })
    .addAllEventBars(eventBars.map { bar -> bar.toDto() })
    .build()

fun EventStatusInternal.toDto(): EventStatusDto {
    return when (this) {
        EventStatusInternal.CREATED -> EventStatusDto.EventStatusDto_Created
        EventStatusInternal.IN_PROGRESS -> EventStatusDto.EventStatusDto_InProgress
        EventStatusInternal.FINISHED -> EventStatusDto.EventStatusDto_Finished
    }
}

fun EventBarInternal.toDto(): EventBarDto = EventBarDto.newBuilder()
    .setBarId(id)
    .addAllReviews(reviews.map { review -> review.toDto() })
    .build()

fun EventBarReviewInternal.toDto(): EventBarReviewDto = EventBarReviewDto.newBuilder()
    .setUserId(userId)
    .setScore(score)
    .setReviewText(comment)
    .build()

fun Instant.toTimestamp(): Timestamp = Timestamp.newBuilder()
    .setSeconds(epochSecond)
    .setNanos(nano)
    .build()

fun Timestamp.toInstant(): Instant {
    return Instant.ofEpochSecond(seconds, nanos.toLong())
}