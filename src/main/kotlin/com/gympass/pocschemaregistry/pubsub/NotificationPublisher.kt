package com.gympass.pocschemaregistry.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import com.gympass.pocschemaregistry.config.kafka.Topics
import com.gympass.pocschemaregistry.models.Person
import com.gympass.pocschemaregistry.shared.jsonMapper
import com.gympass.pocschemaregistry.shared.personsTopic
import io.confluent.developer.User
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationPublisher(
        private val topics: Topics,
        private val objectMapper: ObjectMapper
) {
  private val producer = createProducer("localhost:9092")

  private fun createProducer(brokers: String): Producer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = brokers
    props["key.serializer"] = StringSerializer::class.java
    props["value.serializer"] = StringSerializer::class.java
    return KafkaProducer<String, String>(props)
  }

  fun notifyTestCreated(person: Person) {
    val userJson = jsonMapper.writeValueAsString(person)

    val futureResult = producer.send(ProducerRecord(personsTopic, userJson))

    futureResult.get()
  }
}
