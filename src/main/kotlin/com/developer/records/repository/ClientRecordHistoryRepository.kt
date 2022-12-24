package com.developer.records.repository

import com.developer.records.domain.ClientRecordHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ClientRecordHistoryRepository : JpaRepository<ClientRecordHistory, UUID>
