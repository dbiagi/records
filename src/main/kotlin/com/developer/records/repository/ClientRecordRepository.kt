package com.developer.records.repository

import com.developer.records.domain.ClientRecord
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface ClientRecordRepository : JpaRepository<ClientRecord, UUID> {
    @Query("SELECT cr FROM ClientRecord cr WHERE cr.client.id = ?1 AND cr.createdAt >= ?2 AND cr.createdAt <= ?3")
    fun findClientRecords(clintId: UUID, since: LocalDateTime, until: LocalDateTime, pageable: Pageable): List<ClientRecord>

    fun findByCreatedAtLessThanEqual(createdAt: LocalDateTime): List<ClientRecord>
}
