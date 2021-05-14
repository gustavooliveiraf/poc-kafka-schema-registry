package com.gympass.pocschemaregistry.config.kafka

import com.gympass.kotlinspringsdk.kafka.GympassKafkaConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
@EnableKafka
class KafkaConfig(
        consumerFactory: ConsumerFactory<String, String>,
        kafkaTemplate: KafkaTemplate<String, String>,
        val topics: Topics,
        @Value("\${spring.kafka.dlt.back-off.initial-interval}") private val initialInterval: Long,
        @Value("\${spring.kafka.dlt.back-off.max-interval}") private val maxInterval: Long,
        @Value("\${spring.kafka.dlt.back-off.max-elapsed-time}") private val maxElapsedTime: Long,
) : GympassKafkaConfig(
  consumerFactory,
  kafkaTemplate,
  initialInterval,
  maxInterval,
  maxElapsedTime
) {
  @Bean
  fun claimedDomainValidationSentTopic(): NewTopic {
    return NewTopic(topics.pocCreated, 1, 1.toShort())
  }
}
