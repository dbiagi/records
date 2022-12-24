package com.developer.records.fixture

import com.developer.records.domain.ClientRecordHistory
import java.math.BigDecimal
import java.time.LocalDateTime.now
import java.util.*

object ClientRecordHistoryFixtures {
    fun getHistories(): List<ClientRecordHistory> = listOf(
        ClientRecordHistory(
            id = UUID.randomUUID(),
            description = "record 1",
            value = BigDecimal.TEN,
            client = ClientRecordFixtures.client,
            now(),
            now()
        ),
        ClientRecordHistory(
            id = UUID.randomUUID(),
            description = "record 2",
            value = BigDecimal.TEN,
            client = ClientRecordFixtures.client,
            now(),
            now()
        )

    )
}
