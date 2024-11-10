package ru.nascar.bms.event.controller.mapping

import com.google.protobuf.Timestamp
import ru.nascar.bms.event.service.model.EventInternal
import ru.nascar.bms.event.service.model.EventStatusInternal
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
    .addAllBarIds(eventBars)
    .build()

fun EventStatusInternal.toDto(): EventStatusDto {
    return when (this) {
        EventStatusInternal.CREATED -> EventStatusDto.EventStatusDto_Created
        EventStatusInternal.IN_PROGRESS -> EventStatusDto.EventStatusDto_InProgress
        EventStatusInternal.FINISHED -> EventStatusDto.EventStatusDto_Finished
    }
}

fun Instant.toTimestamp(): Timestamp = Timestamp.newBuilder()
    .setSeconds(epochSecond)
    .setNanos(nano)
    .build()

fun Timestamp.toInstant(): Instant {
    return Instant.ofEpochSecond(seconds, nanos.toLong())
}