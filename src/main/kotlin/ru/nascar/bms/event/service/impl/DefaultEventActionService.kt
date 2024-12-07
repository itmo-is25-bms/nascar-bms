package ru.nascar.bms.event.service.impl

import org.springframework.dao.DuplicateKeyException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.nascar.bms.bar_summary.service.BarSummaryService
import ru.nascar.bms.event.domain.factories.EventBarReviewFactory
import ru.nascar.bms.event.domain.factories.EventReceiptFactory
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.service.EventActionService
import ru.nascar.bms.receipt.service.ReceiptService
import java.time.Clock

@Service
class DefaultEventActionService(
    private val receiptService: ReceiptService,
    private val eventRepository: EventRepository,
    private val clock: Clock,
    private val barSummaryService: BarSummaryService,
) : EventActionService {
    @Transactional
    override fun start(eventId: String, userId: String) {
        val event = eventRepository.getById(eventId)

        event.start(startedBy = userId, startedAt = clock.instant())

        eventRepository.save(event)
    }

    @Transactional
    override fun addReceipt(eventId: String, barId: String, userId: String, receiptData: ByteArray) {
        val event = eventRepository.getById(eventId)

        val receipt = receiptService.create(receiptData = receiptData, createdBy = userId)
        val eventReceipt = EventReceiptFactory.createNew(
            eventId = event.id,
            barId = barId,
            receiptId = receipt.id,
            createdBy = userId,
            createdAt = clock.instant(),
        )

        event.addReceipt(eventReceipt)

        eventRepository.save(event)
    }

    @Transactional
    @Retryable(
        include = [DuplicateKeyException::class],
        backoff = Backoff(random = true, delay = 50, maxDelay = 200)
    )
    override fun addReview(eventId: String, barId: String, userId: String, score: Int, reviewText: String) {
        val event = eventRepository.getById(eventId)

        val review = EventBarReviewFactory.createNew(
            eventId = eventId,
            barId = barId,
            score = score,
            comment = reviewText,
            createdBy = userId,
            createdAt = clock.instant(),
        )
        event.addReview(review)

        eventRepository.save(event)

        // TODO: async update bar summary
        // barSummaryService.updateBarSummary(barId = barId)
    }

    @Transactional
    override fun finish(eventId: String, userId: String) {
        val event = eventRepository.getById(eventId)

        event.finish(finishedBy = userId, finishedAt = clock.instant())

        eventRepository.save(event)
    }
}