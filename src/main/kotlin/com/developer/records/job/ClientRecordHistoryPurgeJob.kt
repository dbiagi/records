package com.developer.records.job

import com.developer.records.service.ClientRecordHistoryFilePurgeService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ClientRecordHistoryPurgeJob(
    private val clientRecordHistoryFilePurgeService: ClientRecordHistoryFilePurgeService
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun purgeOldHistoryFiles() {
        clientRecordHistoryFilePurgeService.purgeOldFiles()
    }
}
