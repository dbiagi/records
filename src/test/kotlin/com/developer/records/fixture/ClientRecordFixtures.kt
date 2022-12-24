package com.developer.records.fixture

import com.developer.records.domain.Client
import com.developer.records.domain.ClientRecord
import java.math.BigDecimal
import java.time.LocalDateTime.now
import java.util.*

object ClientRecordFixtures {
    val client: Client = Client(UUID.randomUUID(), "client 1", now(), now())

    fun getRecords() = listOf(
        ClientRecord(
            id = UUID.randomUUID(),
            description = "record 1",
            value = BigDecimal.TEN,
            client = client,
            now()
        ),
        ClientRecord(
            id = UUID.randomUUID(),
            description = "record 2",
            value = BigDecimal.TEN,
            client = client,
            now()
        )
    )
}
