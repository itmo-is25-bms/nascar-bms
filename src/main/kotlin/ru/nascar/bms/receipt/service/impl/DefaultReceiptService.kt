package ru.nascar.bms.receipt.service.impl

import org.springframework.stereotype.Service
import ru.nascar.bms.receipt.domain.factories.ReceiptFactory
import ru.nascar.bms.receipt.repository.ReceiptRepository
import ru.nascar.bms.receipt.service.ReceiptService
import ru.nascar.bms.receipt.service.model.ReceiptInternal
import java.time.Clock

@Service
class DefaultReceiptService(
    private val receiptRepository: ReceiptRepository,
    private val clock: Clock,
) : ReceiptService {
    override fun create(receiptData: ByteArray, createdBy: String): ReceiptInternal {
        val receipt = ReceiptFactory.createNew(
            receiptData = receiptData,
            createdBy = createdBy,
            createdAt = clock.instant(),
        )

        receiptRepository.save(receipt)

        return ReceiptInternal.fromDomain(receipt)
    }
}