package com.gympass.pocschemaregistry.pubsub.representations

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.*

abstract class BaseEvent(private val eventName: String) {

  @JsonProperty("_app_id")
  val appId: String = "poc-schema-registry"

  @JsonProperty("_event_id")
  val eventId: UUID = UUID.randomUUID()

  @JsonProperty("_event_time")
  val eventTime: Long = Instant.now().toEpochMilli()

  @JsonProperty("_event_type")
  val eventType: String = eventName
}
