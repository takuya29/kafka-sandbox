package org.example.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.example.common.KafkaTopics
import org.example.common.PropertyUtils
import org.slf4j.LoggerFactory
import java.util.UUID

class ProducerApp {
    fun run() {
        val properties =
            PropertyUtils.readConfig("client.properties").apply {
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            }
        KafkaProducer<String, String>(properties).use { kafkaProducer ->
            while (true) {
                val uuid = UUID.randomUUID().toString()
                try {
                    kafkaProducer.send(ProducerRecord(KafkaTopics.KAFKA_SANDBOX, uuid, uuid))
                    LOG.info("Sent: $uuid")
                } catch (e: Exception) {
                    LOG.error("Failed to send record", e)
                }
                Thread.sleep(1000)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ProducerApp::class.java)
    }
}

fun main() {
    ProducerApp().run()
}
