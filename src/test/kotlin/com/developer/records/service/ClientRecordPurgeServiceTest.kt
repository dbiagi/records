package com.developer.records.service

import com.developer.records.domain.ClientRecordHistory
import com.developer.records.fixture.ClientRecordFixtures
import com.developer.records.fixture.ClientRecordHistoryFixtures
import com.developer.records.repository.ClientRecordHistoryRepository
import com.developer.records.repository.ClientRecordRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.*

class ClientRecordPurgeServiceTest {
    private val clientRecordRepository: ClientRecordRepository = mock()
    private val clientRecordHistoryRepository: ClientRecordHistoryRepository = mock()
    private val clientRecordPurgeService: ClientRecordPurgeService = ClientRecordPurgeService(clientRecordRepository, clientRecordHistoryRepository)

    @Test
    fun `given a list of old client records should save as record history`() {
        // given
        val givenRecords = ClientRecordFixtures.getRecords()

        // and
        `when`(clientRecordRepository.findByCreatedAtBeforeEquals(any()))
            .thenReturn(givenRecords)

        // when
        clientRecordPurgeService.purge(10)

        // then
        verify(clientRecordHistoryRepository).saveAll(check { savedRecords: List<ClientRecordHistory> ->
            val savedIds = savedRecords.map { it.id }
            val givenIds = givenRecords.map { it.id }

            assertTrue(savedIds == givenIds)
        })
    }

    @Test
    fun `given a list of old records should save to history and delete the same amount of records`() {
        // given
        val givenRecords = ClientRecordFixtures.getRecords()
        val givenRecordsHistory = ClientRecordHistoryFixtures.getHistories()

        // and
        `when`(clientRecordRepository.findByCreatedAtBeforeEquals(any()))
            .thenReturn(givenRecords)

        `when`(clientRecordHistoryRepository.saveAll(any<List<ClientRecordHistory>>()))
            .thenReturn(givenRecordsHistory)

        // when
        clientRecordPurgeService.purge(10)

        // then
        verify(clientRecordRepository).deleteAllById(check { deletedIds: List<UUID> ->
            assertTrue(deletedIds.size == givenRecords.size)
        })
        verify(clientRecordHistoryRepository).saveAll(any<List<ClientRecordHistory>>())
    }

}
