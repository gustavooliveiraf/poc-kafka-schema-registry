package com.gympass.pocschemaregistry.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.gympass.pocschemaregistry.config.kafka.Topics
import org.springframework.beans.factory.annotation.Autowired

abstract class BaseConsumer {
  @Autowired
  lateinit var objectMapper: ObjectMapper
  @Autowired
  lateinit var topics: Topics

  inline fun <reified T> stringToObject(message: String): T {
    try {
      return objectMapper.readValue(message, T::class.java)
    } catch (ex: MismatchedInputException) {
      println("error!!!!!!")
      throw Error(ex.message)
    }
  }
}
