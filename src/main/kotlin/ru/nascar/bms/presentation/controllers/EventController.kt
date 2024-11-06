package ru.nascar.bms.presentation.controllers

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.presentation.abstractions.EventServiceGrpcKt

@GrpcService
class EventController : EventServiceGrpcKt.EventServiceCoroutineImplBase() {
}