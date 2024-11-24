package ru.nascar.bms.event.controller

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.event.service.EventActionService
import ru.nascar.bms.presentation.abstractions.EventActionServiceGrpcKt
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.AddReceiptCommand
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.AddReceiptCommandResponse
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.AddReviewCommand
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.AddReviewCommandResponse
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.FinishCommand
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.FinishCommandResponse
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.StartCommand
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto.StartCommandResponse

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
            reviewText = request.review.reviewText
        )
        return AddReviewCommandResponse.getDefaultInstance()
    }

    override suspend fun finish(request: FinishCommand): FinishCommandResponse {
        eventActionService.finish(eventId = request.eventId, userId = request.userId)
        return FinishCommandResponse.getDefaultInstance()
    }
}