package com.gympass.pocschemaregistry.services

import com.gympass.pocschemaregistry.models.Person
import com.gympass.pocschemaregistry.pubsub.NotificationPublisher
import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import org.springframework.stereotype.Service

import io.confluent.developer.User

@Service
class PocService(
  private val publisher: NotificationPublisher
) {
  fun emitEvent(person: Person): Person {
    publisher.notifyTestCreated(person)
    return person
  }
}
