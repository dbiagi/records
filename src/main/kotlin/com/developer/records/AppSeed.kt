package com.developer.records

import com.developer.records.domain.Balance
import com.developer.records.domain.Client
import com.developer.records.domain.ClientRecord
import com.developer.records.domain.ClientRecordHistory
import com.developer.records.repository.BalanceRepository
import com.developer.records.repository.ClientRecordHistoryRepository
import com.developer.records.repository.ClientRecordRepository
import com.developer.records.repository.ClientRepository
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.FakerConfig
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime.now
import java.util.*

@Profile("!prod")
@Component
class AppSeed(
    private val clientRecordRepository: ClientRecordRepository,
    private val clientRepository: ClientRepository,
    private val balanceRepository: BalanceRepository,
    private val clientRecordHistoryRepository: ClientRecordHistoryRepository
) : ApplicationRunner {

    val faker: Faker = Faker(FakerConfig.builder().withLocale("pt-BR").build())

    @Transactional
    override fun run(args: ApplicationArguments) {
        if (!args.optionNames.contains("seed")) {
            return
        }

        if (!args.optionNames.contains("no-truncate")) {
            clientRecordRepository.deleteAll()
            balanceRepository.deleteAll()
            clientRepository.deleteAll()
            clientRecordHistoryRepository.deleteAll()
        }

        createClients().forEach {
            createRecords(it)
            createHistory(it)
        }
    }

    private fun createClients(): Collection<Client> {
        return (1..faker.random.nextInt(60, 100))
            .map { Client(null, faker.name.name(), now(), now()) }
            .let { clientRepository.saveAll(it) }
    }

    private fun createHistory(client: Client) {
        (1 .. faker.random.nextInt(50, 100))
            .map {
                ClientRecordHistory(
                    UUID.randomUUID(),
                    faker.electricalComponents.electromechanical(),
                    BigDecimal.valueOf(faker.random.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP),
                    client,
                    now().minusDays(faker.random.nextLong(90) + 90),
                    now()
                )
            }
            .let { clientRecordHistoryRepository.saveAll(it) }
    }


    private fun createRecords(client: Client) {
        (1..faker.random.nextInt(3, 10))
            .map {
                ClientRecord(
                    null,
                    faker.electricalComponents.electromechanical(),
                    BigDecimal.valueOf(faker.random.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP),
                    client,
                    now().minusDays(faker.random.nextLong(90))
                )
            }
            .let { clientRecordRepository.saveAll(it) }
            .sumOf { it.value }
            .let { sumOfRecords -> Balance(null, client, sumOfRecords, now()) }
            .let { balance -> balanceRepository.save(balance) }
    }
}
