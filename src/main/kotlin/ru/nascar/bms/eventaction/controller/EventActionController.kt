package ru.nascar.bms.eventaction.controller

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.presentation.abstractions.EventActionServiceGrpcKt

@GrpcService
class EventActionController : EventActionServiceGrpcKt.EventActionServiceCoroutineImplBase() {
}