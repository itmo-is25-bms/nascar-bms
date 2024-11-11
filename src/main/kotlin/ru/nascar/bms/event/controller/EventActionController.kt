package ru.nascar.bms.event.controller

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.event.service.EventActionService
import ru.nascar.bms.presentation.abstractions.EventActionServiceGrpcKt
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.*

@GrpcService
class EventActionController(
    private val eventActionService: EventActionService
) : EventActionServiceGrpcKt.EventActionServiceCoroutineImplBase() {
    override suspend fun start(request: StartCommand): StartCommandResponse {
        eventActionService.start(eventId = request.eventId, userId = request.userId)
        return StartCommandResponse.getDefaultInstance()
    }

    override suspend fun addReceipt(request: AddReceiptCommand): AddReceiptCommandResponse {
        eventActionService.addReceipt(
            eventId = request.eventId,
            barId = request.barId,
            userId = request.userId,
            receiptData = request.receiptData.toByteArray(),
        )
        return AddReceiptCommandResponse.getDefaultInstance()
    }

    override suspend fun addReview(request: AddReviewCommand): AddReviewCommandResponse {
        eventActionService.addReview(
            eventId = request.eventId,
            barId = request.barId,
            userId = request.userId,
            score = request.review.score,
            reviewText = if (request.review.hasReviewText()) request.review.reviewText else ""
        )
        return AddReviewCommandResponse.getDefaultInstance()
    }

    override suspend fun finish(request: FinishCommand): FinishCommandResponse {
        eventActionService.finish(eventId = request.eventId, userId = request.userId)
        return FinishCommandResponse.getDefaultInstance()
    }
}