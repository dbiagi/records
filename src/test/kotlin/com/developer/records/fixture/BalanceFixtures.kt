package com.developer.records.fixture

import com.developer.records.domain.Balance
import com.developer.records.domain.Client
import java.math.BigDecimal
import java.time.LocalDateTime.now
import java.util.*

object BalanceFixtures {
    fun getBalance() = Balance(
        id = UUID.randomUUID(),
        client = Client(UUID.randomUUID(), "", now(), now()),
        total = BigDecimal.TEN,
        updatedAt = now()
    )
}
