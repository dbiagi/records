package com.developer.records.service

import com.developer.records.domain.ClientRecord
import com.developer.records.domain.ClientRecordHistory
import com.developer.records.repository.ClientRecordHistoryRepository
import com.developer.records.repository.ClientRecordRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class ClientRecordPurgeService(
    private val clientRecordRepository: ClientRecordRepository,
    private val clientRecordHistoryRepository: ClientRecordHistoryRepository
) {
    val logger = KotlinLogging.logger { }

    fun purge(daysToPurge: Int) {
        val dateLimit = now().minusDays(daysToPurge.toLong())

        clientRecordRepository.findByCreatedAtLessThanEqual(dateLimit)
            .map { mapToHistory(it) }
            .let {
                logger.info("saving {} client record on history storage", it.size)
                clientRecordHistoryRepository.saveAll(it)
            }
            .map { it.id }
            .let {
                logger.info("deleting client records older than {} days. Total to delete: {}", daysToPurge, it.size)
                clientRecordRepository.deleteAllById(it)
            }
    }

    fun mapToHistory(clientRecord: ClientRecord): ClientRecordHistory = ClientRecordHistory(
        id = clientRecord.id ?: throw RuntimeException("Unexpected null id"),
        description = clientRecord.description,
        value = clientRecord.value,
        client = clientRecord.client,
        createdAt = clientRecord.createdAt,
        purgedAt = now()
    )
}
