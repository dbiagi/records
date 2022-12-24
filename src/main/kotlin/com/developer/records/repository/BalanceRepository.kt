package com.developer.records.repository

import com.developer.records.domain.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BalanceRepository : JpaRepository<Balance, UUID> {
    fun findOneByClientId(clientId: UUID): Balance?
}
