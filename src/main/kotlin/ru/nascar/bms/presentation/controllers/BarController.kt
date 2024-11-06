package ru.nascar.bms.presentation.controllers

import net.devh.boot.grpc.server.service.GrpcService
import ru.nascar.bms.presentation.abstractions.BarServiceGrpcKt
import ru.nascar.bms.presentation.abstractions.BarServiceProto
import ru.nascar.bms.presentation.abstractions.BarServiceProto.BarDto
import ru.nascar.bms.presentation.abstractions.BarServiceProto.GetByIdQueryResponse

@GrpcService
class BarController: BarServiceGrpcKt.BarServiceCoroutineImplBase() {
    override suspend fun getById(request: BarServiceProto.GetByIdQuery): BarServiceProto.GetByIdQueryResponse {
        val bar = BarDto.newBuilder()
            .setId("test-bar")
            .setName("Test bar")
            .setAddress("City A, street B, house C")
            .build()

        val response = GetByIdQueryResponse.newBuilder()
            .setBar(bar)
            .build()

        return response
    }
}