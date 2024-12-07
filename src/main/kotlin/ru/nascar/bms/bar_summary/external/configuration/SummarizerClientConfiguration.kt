package ru.nascar.bms.bar_summary.external.configuration

import net.devh.boot.grpc.client.inject.GrpcClient
import net.devh.boot.grpc.client.inject.GrpcClientBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nascar.bms.bar_summary.external.impl.DefaultSummarizerClient
import ru.nascar.bms.presentation.abstractions.SummaryServiceGrpc

@Configuration
@GrpcClientBean(
    clazz = SummaryServiceGrpc.SummaryServiceBlockingStub::class,
    beanName = "blockingStub",
    client = GrpcClient("bmsReviewSummarizer")
)
class SummarizerClientConfiguration {
    @Bean
    fun summarizerClientBean(@Autowired blockingStub: SummaryServiceGrpc.SummaryServiceBlockingStub?): DefaultSummarizerClient {
        return DefaultSummarizerClient(blockingStub!!)
    }
}