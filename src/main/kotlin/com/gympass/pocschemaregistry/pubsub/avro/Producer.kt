package com.gympass.pocschemaregistry.pubsub.avro

import com.github.javafaker.Faker
import com.gympass.pocschemaregistry.models.Person
import com.gympass.pocschemaregistry.shared.personsAvroTopic
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.LogManager
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    Producer("localhost:9092", "http://localhost:8081").produce(1)
}

class Producer(brokers: String, schemaRegistryUrl: String) {
    private val producer = createProducer(brokers, schemaRegistryUrl)
    private val schema = Schema.Parser().parse(File("src/main/resources/person.avsc"))

    private fun createProducer(brokers: String, schemaRegistryUrl: String): Producer<String, GenericRecord> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java
        props["value.serializer"] = KafkaAvroSerializer::class.java
        props["schema.registry.url"] = schemaRegistryUrl
        return KafkaProducer<String, GenericRecord>(props)
    }

    fun produce(ratePerSecond: Int) {
        val waitTimeBetweenIterationsMs = 2000L / ratePerSecond

        val faker = Faker()
        while (true) {
            val fakePerson = Person(
                    name = faker.name().fullName(),
                    age = faker.number().numberBetween(18, 90)
            )

            val avroPerson = GenericRecordBuilder(schema).apply {
                set("name", fakePerson.name)
                set("age", fakePerson.age)
            }.build()

            val futureResult = producer.send(ProducerRecord(personsAvroTopic, fakePerson.age.toString(), avroPerson))

            Thread.sleep(waitTimeBetweenIterationsMs)

            futureResult.get()
        }
    }
}
