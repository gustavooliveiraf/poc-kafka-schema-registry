package com.gympass.pocschemaregistry.services

import com.gympass.pocschemaregistry.pubsub.NotificationPublisher
import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import org.springframework.stereotype.Service

@Service
class PocService(
  private val publisher: NotificationPublisher
) {
  fun emitEvent(str: String): PocEvent {
    val event = PocEvent(str)
    publisher.notifyTestCreated(event)
    return event
  }

  fun processEvent(event: PocEvent) {
    println("----------------------")
    println(event)
    println("======================")
  }
}
