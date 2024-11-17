package ru.nascar.bms

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseTearDown
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.nascar.bms.presentation.abstractions.BarServiceGrpc
import java.time.Clock

@SpringBootTest(
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.server.port=9091",
        "grpc.client.barService.address=in-process:test"
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
@AutoConfigureMockMvc
@EnableAutoConfiguration
@DatabaseTearDown(
    value = ["/db/tear-down.xml"],
    type = DatabaseOperation.DELETE_ALL
)
class NascarBmsIntegrationTest {
    @Autowired
    protected lateinit var clock: Clock
    @GrpcClient("barService")
    protected lateinit var barServiceGrpc: BarServiceGrpc.BarServiceBlockingStub

    companion object {
        const val USER_ID = "testUser"
    }
}
