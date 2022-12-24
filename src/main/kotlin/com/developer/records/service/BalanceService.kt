package com.developer.records.service

import com.developer.records.domain.Balance
import com.developer.records.repository.BalanceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BalanceService(
    private val balanceRepository: BalanceRepository
) {
    fun getBalance(clientId: UUID): Balance? = balanceRepository.findOneByClientId(clientId)
}
