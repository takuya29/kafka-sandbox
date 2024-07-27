package org.example.springproducer

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.example.common.PropertyUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import java.util.Properties

@Configuration
@EnableKafka
open class SpringProducerConfig {
    @Bean
    open fun producerFactory(): ProducerFactory<String, String> {
        val propertyMap = getProducerProperties().map { it.key.toString() to it.value }.toMap()
        return DefaultKafkaProducerFactory(propertyMap)
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

    @Bean
    open fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory)
    }

    companion object {
        private fun getProducerProperties(): Properties {
            val properties =
                PropertyUtils.readConfig("client.properties").apply {
                    put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                    put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                }
            return properties
        }
    }
}
