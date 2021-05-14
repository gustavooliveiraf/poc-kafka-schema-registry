package com.gympass.pocschemaregistry.pubsub

import org.apache.kafka.clients.consumer.ConsumerRecord
import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import com.gympass.pocschemaregistry.services.PocService
import com.gympass.pocschemaregistry.shared.annotations.WorkerMode
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener

import io.confluent.developer.User;

@WorkerMode
class NotificationConsumer(
  private val notificationService: PocService,
  ) : BaseConsumer() {
  private val logger = LoggerFactory.getLogger(NotificationConsumer::class.java)

  @KafkaListener(
    beanRef = "self",
    topics = ["#{self.topics.pocCreated}"]
  )
  fun consumePocCreated(record: ConsumerRecord<String, User>) {
    logger.info("Message received on topic ${topics.pocCreated}. Message = ${record.value()}")
//    val event = stringToObject<PocEvent>(message)
    println("====================")
//    notificationService.processEvent(event)
  }
}
