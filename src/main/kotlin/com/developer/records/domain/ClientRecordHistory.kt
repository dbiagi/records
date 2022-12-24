package com.developer.records.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "client_record_history")
data class ClientRecordHistory (
    @Id
    val id: UUID,

    val description: String,

    val value: BigDecimal,

    @ManyToOne
    val client: Client,

    val createdAt: LocalDateTime,

    val purgedAt: LocalDateTime
)
