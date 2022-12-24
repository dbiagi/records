package com.developer.records.service

import com.developer.records.domain.RecordsFilter
import com.developer.records.fixture.ClientRecordFixtures
import com.developer.records.repository.ClientRecordRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.LocalDateTime.now
import java.util.*

class ClientRecordServiceTest {
    private val clientRecordRepository: ClientRecordRepository = mock()

    private fun clientRecordService(): ClientRecordService = ClientRecordService(clientRecordRepository)

    @Test
    fun `given a list o records should return a valid record response`() {
        // given
        val clientRecordService = clientRecordService()
        val clientId = UUID.randomUUID()
        val filters = RecordsFilter(1, now(), now())
        val records = ClientRecordFixtures.getRecords()

        // and
        `when`(clientRecordRepository.findClientRecords(any(), any(), any(), any()))
            .thenReturn(records)

        // when
        val result = clientRecordService.getRecords(clientId, filters)

        // then
        assertTrue(result.records.isNotEmpty())
        verify(clientRecordRepository).findClientRecords(any(), any(), any(), any())
    }

    @Test
    fun `given an empty list o records should return a valid record response`() {
        // given
        val clientRecordService = clientRecordService()
        val clientId = UUID.randomUUID()
        val filters = RecordsFilter(1, now(), now())

        // and
        `when`(clientRecordRepository.findClientRecords(any(), any(), any(), any()))
            .thenReturn(listOf())

        // when
        val result = clientRecordService.getRecords(clientId, filters)

        // then
        assertTrue(result.records.isEmpty())
        verify(clientRecordRepository).findClientRecords(any(), any(), any(), any())
    }
}
