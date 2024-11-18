package ru.nascar.bms.event.service.impl

import org.springframework.dao.DuplicateKeyException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.nascar.bms.event.domain.exception.ReviewAlreadyExistsException
import ru.nascar.bms.event.domain.factories.EventBarReviewFactory
import ru.nascar.bms.event.domain.factories.EventReceiptFactory
import ru.nascar.bms.event.repository.EventBarReviewRepository
import ru.nascar.bms.event.repository.EventRepository
import ru.nascar.bms.event.service.EventActionService
import ru.nascar.bms.receipt.service.ReceiptService
import java.time.Clock

@Service
class DefaultEventActionService(
    private val receiptService: ReceiptService,
    private val eventRepository: EventRepository,
    private val eventBarReviewRepository: EventBarReviewRepository,
    private val clock: Clock,
) : EventActionService {
    override fun start(eventId: String, userId: String) {
        val event = eventRepository.findById(eventId)!!

        event.start(startedBy = userId, startedAt = clock.instant())

        eventRepository.save(event)
    }

    override fun addReceipt(eventId: String, barId: String, userId: String, receiptData: ByteArray) {
        val event = eventRepository.findById(eventId)!!

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
        eventRepository.findById(eventId)!!

        val existingReview = eventBarReviewRepository.findByEventBarAndUser(
            eventId = eventId,
            barId = barId,
            userId = userId
        )

        if (existingReview != null) {
            throw ReviewAlreadyExistsException.byEventBarAndUser(eventId, barId, userId)
        }

        val review = EventBarReviewFactory.createNew(
            eventId = eventId,
            barId = barId,
            score = score,
            comment = reviewText,
            createdBy = userId,
            createdAt = clock.instant(),
        )

        eventBarReviewRepository.save(review)
    }

    override fun finish(eventId: String, userId: String) {
        val event = eventRepository.findById(eventId)!!

        event.finish(finishedBy = userId, finishedAt = clock.instant())

        eventRepository.save(event)
    }
}