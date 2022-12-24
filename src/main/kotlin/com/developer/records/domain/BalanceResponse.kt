package com.developer.records.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class BalanceResponse(val balance: BigDecimal, val from: LocalDateTime)
