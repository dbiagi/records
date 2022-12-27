package com.developer.records.controller

import com.developer.records.config.Exchanges
import com.developer.records.config.Queues
import com.developer.records.domain.GenerateRecordHistory
import com.developer.records.repository.ClientRepository
import com.developer.records.service.ClientRecordHistoryService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream
import java.util.*

@RestController
class ClientRecordHistoryController(
    private val rabbitTemplate: RabbitTemplate,
    private val clientRepository: ClientRepository,
    private val historyService: ClientRecordHistoryService) {

    @PostMapping("/clients/{clientId}/records/history")
    fun history(@PathVariable clientId: UUID): ResponseEntity<Void> {
        val client = clientRepository.findClientById(clientId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val payload = ObjectMapper().writeValueAsString(GenerateRecordHistory(clientId = client.id!!))

        rabbitTemplate.convertAndSend(Exchanges.GENERATE_RECORDS_FILE, Queues.GENERATE_RECORDS_FILE, payload)

        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/clients/{clientId}/records/history/download")
    fun download(@PathVariable clientId: UUID): ResponseEntity<InputStreamResource> {
        clientRepository.findClientById(clientId) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val file = historyService.getFile(clientId)

        if (!file.exists()) return ResponseEntity(HttpStatus.NOT_FOUND)

        val resource = InputStreamResource(FileInputStream(file))

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(file.length())
            .body(resource)
    }
}
