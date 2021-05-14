package com.gympass.pocschemaregistry.controllers

import com.gympass.pocschemaregistry.pubsub.representations.PocEvent
import com.gympass.pocschemaregistry.services.PocService
import com.gympass.pocschemaregistry.shared.annotations.ApiMode
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@ApiMode
@RequestMapping("/poc/{str}")
class PocControllerImpl(
  private val blockedDomainService: PocService
) {
  private val logger = LoggerFactory.getLogger(PocControllerImpl::class.java)

  @GetMapping
  fun getPoc(
    @PathVariable str: String
  ): PocEvent {
    return blockedDomainService.emitEvent(str)
  }
}
