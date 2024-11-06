package ru.nascar.bms.events

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.presentation.abstractions.EventServiceGrpcKt
import ru.nascar.bms.presentation.abstractions.EventServiceProto

@GrpcService
class EventController(
    private val eventService: EventRepository
) : EventServiceGrpcKt.EventServiceCoroutineImplBase() {
    override suspend fun create(request: EventServiceProto.CreateCommand): EventServiceProto.CreateCommandResponse {
        return super.create(request)
    }
}