package com.developer.records.controller

import com.developer.records.domain.RecordsResponse
import com.developer.records.fixture.BalanceFixtures
import com.developer.records.service.BalanceService
import com.developer.records.service.ClientRecordService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.*

class ClientControllerTest {
    private val clientRecordsService: ClientRecordService = mock()
    private val balanceService: BalanceService = mock()

    private fun clientController(): ClientController = ClientController(clientRecordsService, balanceService)

    @Test
    fun `given a request without parameters should supplie default values`() {
        // given
        val clientController = clientController()
        val clientId = UUID.randomUUID()
        val parameters = mapOf<String, String>()

        // and
        `when`(clientRecordsService.getRecords(any(), any()))
            .thenReturn(RecordsResponse(mapOf()))

        // when
        val result = clientController.records(clientId, parameters)

        // then
        verify(clientRecordsService).getRecords(any(), any())
        result.statusCode.is2xxSuccessful
    }

    @Test
    fun `given a client id should return client balance with 2xx status`() {
        // given
        val clientController = clientController()
        val clientId = UUID.randomUUID()
        val balance = BalanceFixtures.getBalance()

        // and
        `when`(balanceService.getBalance(clientId)).thenReturn(balance)

        // when
        val result = clientController.balance(clientId)

        // then
        result.statusCode.is2xxSuccessful
        verify(balanceService).getBalance(clientId)
    }

    @Test
    fun `given a inexistent client id should return bad request`() {
        // given
        val clientController = clientController()
        val clientId = UUID.randomUUID()

        // and
        `when`(balanceService.getBalance(clientId)).thenReturn(null)

        // when
        val result = clientController.balance(clientId)

        // then
        result.statusCode.is4xxClientError
        verify(balanceService).getBalance(clientId)
    }

}
