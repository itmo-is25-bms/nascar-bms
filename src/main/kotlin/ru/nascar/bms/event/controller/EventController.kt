package ru.nascar.bms.event.controller

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.event.controller.mapping.toDto
import ru.nascar.bms.event.controller.mapping.toInstant
import ru.nascar.bms.event.service.EventService
import ru.nascar.bms.presentation.abstractions.EventServiceGrpcKt
import ru.nascar.bms.presentation.abstractions.EventServiceProto.AddUserToEventCommand
import ru.nascar.bms.presentation.abstractions.EventServiceProto.AddUserToEventCommandResponse
import ru.nascar.bms.presentation.abstractions.EventServiceProto.CreateCommand
import ru.nascar.bms.presentation.abstractions.EventServiceProto.CreateCommandResponse
import ru.nascar.bms.presentation.abstractions.EventServiceProto.GetByIdQuery
import ru.nascar.bms.presentation.abstractions.EventServiceProto.GetByIdQueryResponse
import ru.nascar.bms.presentation.abstractions.EventServiceProto.GetByPasscodeQuery
import ru.nascar.bms.presentation.abstractions.EventServiceProto.GetByPasscodeQueryResponse
import ru.nascar.bms.presentation.abstractions.EventServiceProto.GetByUserIdQuery
import ru.nascar.bms.presentation.abstractions.EventServiceProto.GetByUserIdQueryResponse
import ru.nascar.bms.presentation.abstractions.EventServiceProto.RemoveUserFromEventCommand
import ru.nascar.bms.presentation.abstractions.EventServiceProto.RemoveUserFromEventCommandResponse

@GrpcService
class EventController(
    private val eventService: EventService
) : EventServiceGrpcKt.EventServiceCoroutineImplBase() {
    override suspend fun create(request: CreateCommand): CreateCommandResponse {
        val eventInternal = eventService.create(
            userId = request.userId,
            name = request.event.name,
            startDatetime = request.event.startDate.toInstant(),
            eventBarsIds = HashSet(request.event.barIdsList),
        )

        val eventDto = eventInternal.toDto()

        return CreateCommandResponse.newBuilder().setEvent(eventDto).build()
    }

    override suspend fun getById(request: GetByIdQuery): GetByIdQueryResponse {
        val eventInternal = eventService.getById(request.eventId)

        val eventDto = eventInternal.toDto()

        return GetByIdQueryResponse.newBuilder().setEvent(eventDto).build()
    }

    override suspend fun getByPasscode(request: GetByPasscodeQuery): GetByPasscodeQueryResponse {
        val eventInternal = eventService.findByPasscode(request.passcode)

        val eventDto = eventInternal?.toDto()

        return when (eventDto){
            null -> GetByPasscodeQueryResponse.getDefaultInstance()
            else -> GetByPasscodeQueryResponse.newBuilder().setEvent(eventDto).build()
        }
    }

    override suspend fun getByUserId(request: GetByUserIdQuery): GetByUserIdQueryResponse {
        val eventsInternal = eventService.getByUserId(request.userId)

        val eventsDto = eventsInternal.map { it.toDto() }

        return GetByUserIdQueryResponse.newBuilder().addAllEvents(eventsDto).build()
    }

    override suspend fun addUserToEvent(request: AddUserToEventCommand): AddUserToEventCommandResponse {
        eventService.addUserToEvent(id = request.eventId, userId = request.userId)

        return AddUserToEventCommandResponse.getDefaultInstance()
    }

    override suspend fun removeUserFromEvent(request: RemoveUserFromEventCommand): RemoveUserFromEventCommandResponse {
        eventService.removeUserFromEvent(id = request.eventId, userId = request.userId)

        return RemoveUserFromEventCommandResponse.getDefaultInstance()
    }
}