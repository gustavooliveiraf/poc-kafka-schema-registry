package com.gympass.pocschemaregistry.pubsub.representations

data class PocEvent(
  val name: String
) : BaseEvent("test")
