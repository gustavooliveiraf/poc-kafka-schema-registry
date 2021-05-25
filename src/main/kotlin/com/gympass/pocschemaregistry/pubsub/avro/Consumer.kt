package com.gympass.pocschemaregistry.pubsub.avro


import com.gympass.pocschemaregistry.models.Person
import com.gympass.pocschemaregistry.shared.personsAvroToJsonTopic
import com.gympass.pocschemaregistry.shared.jsonMapper
import com.gympass.pocschemaregistry.shared.personsAvroTopic
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
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
    Consumer("localhost:9092", "http://localhost:8081").process()
}

class Consumer(brokers: String, schemaRegistryUrl: String) {

    private val logger = LogManager.getLogger(javaClass)
    private val consumer = createConsumer(brokers, schemaRegistryUrl)
    private val producer = createProducer(brokers)

    private fun createConsumer(brokers: String, schemaRegistryUrl: String): Consumer<String, GenericRecord> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["group.id"] = "person-processor"
        props["key.deserializer"] = StringDeserializer::class.java
        props["value.deserializer"] = KafkaAvroDeserializer::class.java
        props["schema.registry.url"] = schemaRegistryUrl
        return KafkaConsumer<String, GenericRecord>(props)
    }

    private fun createProducer(brokers: String): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java
        props["value.serializer"] = StringSerializer::class.java
        return KafkaProducer<String, String>(props)
    }

    fun process() {
        consumer.subscribe(listOf(personsAvroTopic))

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            logger.info("Received ${records.count()} records")

            records.iterator().forEach {
                val personAvro = it.value()

                val person = Person(
                        name = personAvro["name"].toString(),
                        age = personAvro["age"] as Int
                )

                val personJson = jsonMapper.writeValueAsString(person.copy(age = person.age + 10))

                val future = producer.send(ProducerRecord(personsAvroToJsonTopic, personJson))
                future.get()
            }
        }
    }
}
