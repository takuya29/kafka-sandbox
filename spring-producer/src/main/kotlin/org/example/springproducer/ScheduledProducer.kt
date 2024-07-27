package org.example.springproducer

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.TimeUnit

@Component
@EnableScheduling
open class ScheduledProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    open fun produce() {
        val uuid = UUID.randomUUID().toString()
        try {
            kafkaTemplate.send(ProducerRecord(TOPIC, uuid, uuid))
            LOG.info("Sent: $uuid")
        } catch (e: Exception) {
            LOG.error("Failed to send record", e)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ScheduledProducer::class.java)
        private const val TOPIC = "consumer-test"
    }
}
