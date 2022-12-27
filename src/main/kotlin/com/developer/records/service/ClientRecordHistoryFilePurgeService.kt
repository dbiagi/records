package com.developer.records.service

import com.developer.records.config.AppConfig
import org.springframework.stereotype.Service
import java.io.File
import java.time.Instant
import java.util.*

@Service
class ClientRecordHistoryFilePurgeService(
    private val appConfig: AppConfig
) {
    companion object {
        const val OLDER_THAN_DATE: Long = 60 * 24 * 90
    }

    fun purgeOldFiles() {
        val folder = File(appConfig.getStoragePath())

        if (!folder.exists() || !folder.isDirectory) throw RuntimeException("history folder does not exists or is not a repository")

        folder.listFiles()
            ?.filter { Date(it.lastModified()) <= Date.from(Instant.now().minusSeconds(OLDER_THAN_DATE)) }
            ?.forEach { it.delete() }
    }
}
