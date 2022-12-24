package com.developer.records.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Declarables
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.interceptor.RetryInterceptorBuilder.stateless
import org.springframework.retry.interceptor.RetryOperationsInterceptor

@Configuration
@EnableRabbit
class RabbitMqConfiguration(
    private val cachingConnectionFactory: CachingConnectionFactory
) {

    @Bean
    fun rabbitmqMessageConverter() = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(rabbitMessageConverter: Jackson2JsonMessageConverter): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(cachingConnectionFactory)
        rabbitTemplate.messageConverter = rabbitMessageConverter

        return rabbitTemplate
    }

    @Bean
    fun rabbitListenerConnectionFactory(connectionFactory: ConnectionFactory) = SimpleRabbitListenerContainerFactory().apply {
        setMaxConcurrentConsumers(2)
        setConcurrentConsumers(2)
        setConnectionFactory(connectionFactory)
        setDefaultRequeueRejected(false)
        setMissingQueuesFatal(false)
        setAdviceChain(interceptor())
    }

    private fun interceptor(): RetryOperationsInterceptor = stateless()
        .maxAttempts(1)
        .build()

    @Bean
    fun setupQueues(): Declarables {
        val bindings = listOf(
            topic(Queues.ADD_RECORDS, Exchanges.ADD_RECORDS),
            topic(Queues.GENERATE_RECORDS_FILE, Exchanges.GENERATE_RECORDS_FILE)
        )

        return Declarables(
            bindings.flatMap { listOf(it.queue, it.exchange, it.binding) }
        )
    }

    private fun topic(queueName: String, exchangeName: String): Bindings {
        val queue = QueueBuilder
            .durable(queueName)
            .deadLetterExchange("x.dql.$queueName")
            .build()

        val exchange = TopicExchange(exchangeName)

        val binding = BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(queueName)

        return Bindings(queue, exchange, binding)
    }

    data class Bindings(val queue: Queue,
                        val exchange: Exchange,
                        val binding: Binding)
}
