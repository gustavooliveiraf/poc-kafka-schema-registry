package com.gympass.pocschemaregistry.pubsub.json

import com.github.javafaker.Faker
import com.gympass.pocschemaregistry.models.Person
import com.gympass.pocschemaregistry.shared.jsonMapper
import com.gympass.pocschemaregistry.shared.personsTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.LogManager
import java.util.*

fun main(args: Array<String>) {
    Producer("localhost:9092").produce(1)
}

class Producer(brokers: String) {
    private val producer = createProducer(brokers)

    private fun createProducer(brokers: String): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java
        props["value.serializer"] = StringSerializer::class.java
        return KafkaProducer<String, String>(props)
    }

    fun produce(ratePerSecond: Int) {
        val waitTimeBetweenIterationsMs = 2000L / ratePerSecond

        val faker = Faker()
        while (true) {
            val fakePerson = Person(
                    name = faker.name().firstName(),
                    age = faker.number().numberBetween(18, 90)
            )

            val fakePersonJson = jsonMapper.writeValueAsString(fakePerson)

            val futureResult = producer.send(ProducerRecord(personsTopic, fakePersonJson))

            Thread.sleep(waitTimeBetweenIterationsMs)

            futureResult.get()
        }
    }
}
