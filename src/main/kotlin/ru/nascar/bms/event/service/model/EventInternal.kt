package ru.nascar.bms.event.service.model

import ru.nascar.bms.event.domain.model.Event
import java.time.Instant

data class EventInternal(
    val id: String,
    val name: String,
    val status: EventStatusInternal,
    val passcode: String,
    val startDateTime: Instant,
    val eventBars: List<EventBarInternal>,
    val participants: List<String>,
    val receipts: List<String>,
    val createdBy: String,
) {
    companion object {
        fun fromDomain(event: Event): EventInternal {
            return EventInternal(
                id = event.id,
                name = event.name,
                status = EventStatusInternal.fromDomain(event.status),
                passcode = event.passcode,
                startDateTime = event.startDateTime,
                eventBars = event.eventBars.map { bar -> EventBarInternal.fromDomain(bar, event) },
                participants = event.participants.map { participant -> participant.userId },
                receipts = event.receipts.map { receipt -> receipt.receiptId },
                createdBy = event.createdBy,
            )
        }
    }
}