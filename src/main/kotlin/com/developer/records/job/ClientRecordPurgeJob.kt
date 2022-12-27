package com.developer.records.job

import com.developer.records.service.ClientRecordPurgeService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ClientRecordPurgeJob(
    private val clientRecordPurgeService: ClientRecordPurgeService
) {
    companion object {
        const val DAYS_TO_PURGE = 90
    }

    val logger = KotlinLogging.logger { }

    @Scheduled(cron = "0 0 0 * * *")
    fun purgeOldClientRecords() {
        try {
            clientRecordPurgeService.purge(DAYS_TO_PURGE)
        } catch (e: Exception) {
            logger.error("unexpected error when purging old client records", e)
        }
    }
}
