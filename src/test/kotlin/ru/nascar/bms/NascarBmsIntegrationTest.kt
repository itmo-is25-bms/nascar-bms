package ru.nascar.bms

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseTearDown
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.nascar.bms.presentation.abstractions.BarServiceGrpc
import ru.nascar.bms.presentation.abstractions.EventActionServiceGrpc
import ru.nascar.bms.presentation.abstractions.EventServiceGrpc
import ru.nascar.bms.utils.GrpcAssertions
import java.time.Clock

@SpringBootTest(
    properties = [
        "grpc.server.port=9091",
        "grpc.server.inProcessName=test",
        "grpc.client.barService.address=in-process:test",
        "grpc.client.eventService.address=in-process:test",
        "grpc.client.eventActionService.address=in-process:test"
    ]
)
@Transactional(propagation = Propagation.SUPPORTS)
@TestExecutionListeners(
    value = [
        DependencyInjectionTestExecutionListener::class,
        DirtiesContextTestExecutionListener::class,
        TransactionalTestExecutionListener::class,
        DbUnitTestExecutionListener::class,
    ],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@EnableAutoConfiguration
@DatabaseTearDown(
    value = ["/db/tear-down.xml"],
    type = DatabaseOperation.DELETE_ALL
)
class NascarBmsIntegrationTest {
    protected val grpcAssert = GrpcAssertions()

    @Autowired
    protected lateinit var clock: Clock

    @GrpcClient("barService")
    protected lateinit var barServiceGrpc: BarServiceGrpc.BarServiceBlockingStub

    @GrpcClient("eventService")
    protected lateinit var eventServiceGrpc: EventServiceGrpc.EventServiceBlockingStub

    @GrpcClient("eventActionService")
    protected lateinit var eventActionServiceGrpc: EventActionServiceGrpc.EventActionServiceBlockingStub

    companion object {
        const val USER_ID = "testUser"
        const val BAR_ID = "BAR-01"
        const val BAR_NAME = "Рядом"
        const val BAR_ADDRESS = "Гороховая, 32"
    }
}
