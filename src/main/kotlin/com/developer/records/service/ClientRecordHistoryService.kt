package com.developer.records.service

import com.developer.records.config.AppConfig
import com.developer.records.domain.ClientRecordHistory
import com.developer.records.domain.GenerateRecordHistory
import com.developer.records.repository.ClientRecordHistoryRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

@Service
class ClientRecordHistoryService(
    private val clientRecordHistoryRepository: ClientRecordHistoryRepository,
    private val appConfig: AppConfig
) {
    val logger = KotlinLogging.logger { }

    fun generateFile(generateRecordHistory: GenerateRecordHistory) {
        val fileName = appConfig.getStoragePath() + "/history-" + generateRecordHistory.clientId.toString() + ".csv"
        val bw = BufferedWriter(FileWriter(fileName))

        bw.appendLine(headerLine())

        clientRecordHistoryRepository.findAllByClientId(generateRecordHistory.clientId!!)
            .map { formatLine(it) }
            .forEach { bw.appendLine(it) }

        bw.close()

        logger.info("history file created for client {}", generateRecordHistory.clientId)
    }

    fun getFile(clientId: UUID): File {
        val fileName = appConfig.getStoragePath() + "/history-" + clientId.toString() + ".csv"

        return File(fileName)
    }

    private fun headerLine(): String = "id;clientId;description;value;createdAt;purgedAt"

    private fun formatLine(record: ClientRecordHistory): String {
        return """
            "%s";"%s";"%s";"%s";"%s";"%s"
        """
            .trimIndent()
            .format(record.id, record.client.id, record.description, record.value, record.createdAt, record.purgedAt)
    }
}
