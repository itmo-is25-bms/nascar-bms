package ru.nascar.bms.shared

import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler
import ru.nascar.bms.shared.exception.BmsErrorCode
import ru.nascar.bms.shared.exception.BmsException
import ru.nascar.bms.shared.model.Common.BmsError

@GrpcAdvice
class BmsGrpcExceptionHandler {
    @GrpcExceptionHandler(BmsException::class)
    fun handleBmsException(exception: BmsException): StatusRuntimeException {
        val error = BmsError.newBuilder()
            .setMessage(exception.message)
            .setBmsErrorCode(exception.errorCode.code)
            .putAllBmsErrorData(exception.errorData)
            .build()


        return getStatusRuntimeException(error)
    }

    @GrpcExceptionHandler(RuntimeException::class)
    fun handleException(exception: RuntimeException): StatusRuntimeException {
        val error = BmsError.newBuilder()
            .setMessage(exception.message ?: "unknown")
            .setBmsErrorCode(BmsErrorCode.UNKNOWN)
            .build()


        return getStatusRuntimeException(error)
    }

    private fun getStatusRuntimeException(error: BmsError): StatusRuntimeException {
        val status = Status.newBuilder()
            .setMessage(error.message)
            .setCode(Code.ABORTED.number)
            .addDetails(Any.pack(error))
            .build()

        return StatusProto.toStatusRuntimeException(status)
    }
}
