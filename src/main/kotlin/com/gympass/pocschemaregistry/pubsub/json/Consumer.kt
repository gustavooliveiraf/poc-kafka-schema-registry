package com.gympass.pocschemaregistry.pubsub.json

import com.gympass.pocschemaregistry.models.Person
import com.gympass.pocschemaregistry.shared.personsAvroToJsonTopic
import com.gympass.pocschemaregistry.shared.jsonMapper
import com.gympass.pocschemaregistry.shared.personsTopic
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.LogManager
import java.time.Duration
import java.util.*

fun main(args: Array<String>) {
    Consumer("localhost:9092").process()
}

class Consumer(brokers: String) {

    private val logger = LogManager.getLogger(javaClass)
    private val consumer = createConsumer(brokers)
    private val producer = createProducer(brokers)

    private fun createConsumer(brokers: String): Consumer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["group.id"] = "person-processor"
        props["key.deserializer"] = StringDeserializer::class.java
        props["value.deserializer"] = StringDeserializer::class.java
        return KafkaConsumer<String, String>(props)
    }

    private fun createProducer(brokers: String): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java
        props["value.serializer"] = StringSerializer::class.java
        return KafkaProducer<String, String>(props)
    }

    fun process() {
        consumer.subscribe(listOf(personsTopic))

        logger.info("Consuming and processing data")

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            logger.info("Received ${records.count()} records")

            records.iterator().forEach {
                val personJson = it.value()
                logger.debug("JSON data: $personJson")

                val person = jsonMapper.readValue(personJson, Person::class.java)
                logger.debug("Person: $person")

                val newAge = person.age + 1
                logger.debug("Age: $newAge")

                val future = producer.send(ProducerRecord(personsAvroToJsonTopic, "${person.name}", "$newAge"))
                future.get()
            }
        }
    }
}
