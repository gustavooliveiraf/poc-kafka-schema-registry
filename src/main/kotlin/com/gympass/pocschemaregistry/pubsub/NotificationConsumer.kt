package com.gympass.pocschemaregistry.pubsub

import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import com.gympass.pocschemaregistry.services.PocService
import com.gympass.pocschemaregistry.shared.annotations.WorkerMode
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener

@WorkerMode
class NotificationConsumer(
  private val notificationService: PocService,
  ) : BaseConsumer() {
  private val logger = LoggerFactory.getLogger(NotificationConsumer::class.java)

  @KafkaListener(
    beanRef = "self",
    topics = ["#{self.topics.pocCreated}"]
  )
  fun consumePocCreated(message: String) {
    logger.info("Message received on topic ${topics.pocCreated}. Message = $message")
    val event = stringToObject<PocEvent>(message)
    notificationService.processEvent(event)
  }
}
