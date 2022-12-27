package com.developer.records.listener

import com.developer.records.config.Queues
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RecordsListener {
    val logger = KotlinLogging.logger{}

    @RabbitListener(queues = [Queues.ADD_RECORDS])
    fun addRecord(payload: String) {
        logger.info("consuming message $payload")
    }

}
