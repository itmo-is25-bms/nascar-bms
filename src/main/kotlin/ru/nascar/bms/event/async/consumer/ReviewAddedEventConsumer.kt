package ru.nascar.bms.event.async.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import ru.nascar.bms.bar_summary.service.BarSummaryService
import ru.nascar.bms.config.AsyncConfig
import ru.nascar.bms.event.domain.event.ReviewAddedEvent

@Service
class ReviewAddedEventConsumer(
    private val barSummaryService: BarSummaryService,
    private val objectMapper: ObjectMapper,
) {

    private val log = KotlinLogging.logger { }

    @Async(AsyncConfig.EVENT_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun consumeEvent(event: ReviewAddedEvent) {
        log.info { "[BMS] [EventId:${event.eventId}] Consume ReviewAddedEvent: ${objectMapper.writeValueAsString(event)}" }

        runCatching {
            executeInternal(event)
        }.onSuccess {
            log.info { "[BMS] [EventId:${event.eventId}] Event successfully consumed" }
        }.onFailure {
            log.error(it) { "[BMS] [EventId:${event.eventId}] Event consume failed"  }
        }
    }

    private fun executeInternal(event: ReviewAddedEvent) {
        barSummaryService.updateBarSummary(event.barId)
    }
}