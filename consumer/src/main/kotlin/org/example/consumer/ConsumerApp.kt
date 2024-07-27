package org.example.consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.example.common.KafkaTopics
import org.example.common.PropertyUtils
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.UUID

class ConsumerApp {
    fun run() {
        val properties =
            PropertyUtils.readConfig("client.properties").apply {
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-${UUID.randomUUID()}")
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100)
            }

        KafkaConsumer<String, String>(properties).use { kafkaConsumer ->
            kafkaConsumer.subscribe(listOf(KafkaTopics.KAFKA_SANDBOX))

            while (true) {
                try {
                    val records = kafkaConsumer.poll(TIMEOUT_DURATION)
                    val lastOffset =
                        records.groupBy { it.partition() }
                            .mapValues { (_, records) -> records.maxOf { it.offset() } }
                    LOG.info("assignment: ${kafkaConsumer.assignment()}, size: ${records.count()}, lastOffset: $lastOffset")
                } catch (e: Exception) {
                    LOG.error("Failed to receive record", e)
                }

                Thread.sleep(1000)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ConsumerApp::class.java)
        private val TIMEOUT_DURATION = Duration.ofMillis(100)
    }
}

fun main() {
    ConsumerApp().run()
}
