package com.developer.records.listener

import com.developer.records.config.Queues
import com.developer.records.domain.GenerateRecordHistory
import com.developer.records.service.ClientRecordHistoryService
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ClientRecordHistoryListener(
    private val clientRecordHistoryService: ClientRecordHistoryService
) {

    val logger = KotlinLogging.logger { }

    @RabbitListener(queues = [Queues.GENERATE_RECORDS_FILE])
    fun generate(payload: String) {
        logger.info("generating history file with payload={}", payload)

        val generateRequest = ObjectMapper().readValue(payload, GenerateRecordHistory::class.java)
        clientRecordHistoryService.generateFile(generateRequest)
    }
}
