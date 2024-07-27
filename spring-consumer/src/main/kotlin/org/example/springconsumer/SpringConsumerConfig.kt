package org.example.springconsumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer
import org.example.common.PropertyUtils
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.util.Properties
import java.util.UUID

@Configuration
@EnableKafka
open class SpringConsumerConfig {
    @Bean
    open fun consumerFactory(): ConsumerFactory<String, String> {
        val propertyMap = getConsumerProperties().map { it.key.toString() to it.value }.toMap()
        return DefaultKafkaConsumerFactory(propertyMap)
    }

    @Bean
    open fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
    ): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.isBatchListener = true
        return factory
    }

    @KafkaListener(topics = [TOPIC])
    open fun listen(records: ConsumerRecords<String, String>) {
        val lastOffset =
            records.groupBy { it.partition() }
                .mapValues { (_, records) -> records.maxOf { it.offset() } }
        LOG.info("size: ${records.count()}, lastOffset: $lastOffset")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SpringConsumerApp::class.java)
        private const val TOPIC = "consumer-test"

        private fun getConsumerProperties(): Properties {
            val properties =
                PropertyUtils.readConfig("client.properties").apply {
                    put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                    put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                    put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-${UUID.randomUUID()}")
                    put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
                    put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100)
                }
            return properties
        }
    }
}
