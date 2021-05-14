package com.gympass.pocschemaregistry.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.gympass.pocschemaregistry.config.kafka.Topics
import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

import io.confluent.developer.User

@Service
class NotificationPublisher(
  private val kafkaTemplate: KafkaTemplate<String, User>,
  private val topics: Topics,
  private val objectMapper: ObjectMapper
) {
  private val logger = LoggerFactory.getLogger(NotificationPublisher::class.java)

  fun notifyTestCreated(user: User) {
//    val jsonPayload = objectMapper.writeValueAsString(event)
    kafkaTemplate.send(topics.pocCreated, user.getName(), user)

    logger.info("event published. payload= $user")
  }
}
