package com.gympass.pocschemaregistry.controllers

import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import com.gympass.pocschemaregistry.services.PocService
import com.gympass.pocschemaregistry.shared.annotations.ApiMode
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

import io.confluent.developer.User

@ApiMode
@RequestMapping("/poc/{name}/{age}")
class PocControllerImpl(
  private val blockedDomainService: PocService
) {
  private val logger = LoggerFactory.getLogger(PocControllerImpl::class.java)

  @GetMapping
  fun getPoc(
    @PathVariable str: String
  ): String {
    return blockedDomainService.emitEvent(User(name, age))
  }
}
