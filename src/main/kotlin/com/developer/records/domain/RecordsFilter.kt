package com.developer.records.domain

import java.time.LocalDateTime

data class RecordsFilter(
    val page: Int,
    val since: LocalDateTime,
    val until: LocalDateTime)
