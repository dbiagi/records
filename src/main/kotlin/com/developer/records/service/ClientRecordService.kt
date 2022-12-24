package com.developer.records.service

import com.developer.records.domain.RecordResponse
import com.developer.records.domain.RecordsFilter
import com.developer.records.domain.RecordsResponse
import com.developer.records.repository.ClientRecordRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientRecordService(
    private val clientRecordRepository: ClientRecordRepository
) {
    fun getRecords(clientId: UUID, filters: RecordsFilter): RecordsResponse {
        val pageable = PageRequest.of(filters.page - 1, 10, Sort.Direction.DESC, "createdAt")

        return clientRecordRepository
            .findClientRecords(clientId, filters.since, filters.until, pageable)
            .map { RecordResponse(it.description, it.value, it.createdAt.toLocalDate()) }
            .groupBy { it.createdAt.toString() }
            .let { RecordsResponse(it) }
    }
}
