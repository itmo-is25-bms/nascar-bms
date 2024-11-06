package ru.nascar.bms.bars

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.presentation.abstractions.BarServiceGrpcKt
import ru.nascar.bms.presentation.abstractions.BarServiceProto.CreateCommand
import ru.nascar.bms.presentation.abstractions.BarServiceProto.CreateCommandResponse
import ru.nascar.bms.presentation.abstractions.BarServiceProto.GetAllQuery
import ru.nascar.bms.presentation.abstractions.BarServiceProto.GetAllQueryResponse
import ru.nascar.bms.presentation.abstractions.BarServiceProto.GetByIdQuery
import ru.nascar.bms.presentation.abstractions.BarServiceProto.GetByIdQueryResponse
import ru.nascar.bms.bars.converters.BarDtoConverter

@GrpcService
class BarController(
    private val barService: BarService,
) : BarServiceGrpcKt.BarServiceCoroutineImplBase() {
    override suspend fun create(request: CreateCommand): CreateCommandResponse {
        val barInternal = barService.create(
            userId = request.userId, name = request.bar.name, address = request.bar.address
        )

        val bar = BarDtoConverter.fromInternal(barInternal)

        return CreateCommandResponse.newBuilder().setBar(bar).build()
    }

    override suspend fun getById(request: GetByIdQuery): GetByIdQueryResponse {
        val barInternal = barService.getById(request.barId)

        val bar = BarDtoConverter.fromInternal(barInternal)

        return GetByIdQueryResponse.newBuilder().setBar(bar).build()
    }

    override suspend fun getAll(request: GetAllQuery): GetAllQueryResponse {
        val barsInternal = barService.getAll()

        val bars = barsInternal.map { bar -> BarDtoConverter.fromInternal(bar) }

        return GetAllQueryResponse.newBuilder().addAllBars(bars).build()
    }
}