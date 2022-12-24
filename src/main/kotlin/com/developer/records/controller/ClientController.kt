package com.developer.records.controller

import com.developer.records.domain.BalanceResponse
import com.developer.records.domain.RecordsFilter
import com.developer.records.domain.RecordsResponse
import com.developer.records.service.BalanceService
import com.developer.records.service.ClientRecordService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.*

@RestController
class ClientController(
    private val clientRecordService: ClientRecordService,
    private val balanceService: BalanceService
) {
    val logger = KotlinLogging.logger {}

    @GetMapping("/clients/{clientId}/balance")
    fun balance(@PathVariable clientId: UUID): ResponseEntity<BalanceResponse> {
        logger.info("get balance of client $clientId")

        val balance = balanceService.getBalance(clientId)

        return balance?.let { ResponseEntity(BalanceResponse(it.total, it.updatedAt), HttpStatus.OK) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/clients/{clientId}/records")
    fun records(@PathVariable clientId: UUID, @RequestParam params: Map<String, String>): ResponseEntity<RecordsResponse> {
        logger.info("get records of client $clientId with parameters $params")

        val page = params["page"]?.toInt() ?: 1
        val inferiorDateLimit = LocalDateTime.now().minusDays(90)
        val since = params["since"]?.let { LocalDateTime.parse(it) } ?: inferiorDateLimit
        val until = params["until"]?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()

        val validatedSince =  if (since < inferiorDateLimit) inferiorDateLimit else since

        val filters = RecordsFilter(page, validatedSince, until)

        val records = clientRecordService.getRecords(clientId, filters)

        return ResponseEntity(records, HttpStatus.OK)
    }
}
