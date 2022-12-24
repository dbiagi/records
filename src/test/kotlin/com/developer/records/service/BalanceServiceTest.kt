package com.developer.records.service

import com.developer.records.repository.BalanceRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.*

class BalanceServiceTest {
    private val balanceRepository: BalanceRepository = mock()

    private fun balanceService(): BalanceService = BalanceService(balanceRepository)

    @Test
    fun `given a client id should call the repository`() {
        // given
        val balanceService = balanceService()
        val clientId = UUID.randomUUID()

        // when
        balanceService.getBalance(clientId)

        // then
        verify(balanceRepository).findOneByClientId(clientId)
    }
}
