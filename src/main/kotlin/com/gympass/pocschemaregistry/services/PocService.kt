package com.gympass.pocschemaregistry.services

import com.gympass.pocschemaregistry.pubsub.NotificationPublisher
import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import org.springframework.stereotype.Service

import io.confluent.developer.User

@Service
class PocService(
  private val publisher: NotificationPublisher
) {
  fun emitEvent(user: User): String {
    publisher.notifyTestCreated(user)
    return "user"
  }

  fun processEvent(event: PocEvent) {
    println("----------------------")
    println(event)
    println("======================")
  }
}
