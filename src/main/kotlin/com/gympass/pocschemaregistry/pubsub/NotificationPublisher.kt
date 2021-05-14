package com.gympass.pocschemaregistry.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.gympass.pocschemaregistry.config.kafka.Topics
import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class NotificationPublisher(
  private val kafkaTemplate: KafkaTemplate<String, String>,
  private val topics: Topics,
  private val objectMapper: ObjectMapper
) {
  private val logger = LoggerFactory.getLogger(NotificationPublisher::class.java)

  fun notifyTestCreated(event: PocEvent) {
    val jsonPayload = objectMapper.writeValueAsString(event)
    kafkaTemplate.send(topics.pocCreated, jsonPayload)

    logger.info("event published. payload=$jsonPayload")
  }
}
