package com.developer.records.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.time.LocalDate

data class RecordsResponse(
    val records: Map<String, List<RecordResponse>>
)

data class RecordResponse(
    val description: String,
    val value: BigDecimal,

    @get:JsonIgnore
    val createdAt: LocalDate
)
