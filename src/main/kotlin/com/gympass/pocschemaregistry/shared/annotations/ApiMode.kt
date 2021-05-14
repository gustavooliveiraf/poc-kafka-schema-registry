package com.gympass.pocschemaregistry.shared.annotations

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.RestController
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE, ElementType.METHOD)
@ConditionalOnProperty(value = ["pocschemaregistry.mode.api"], havingValue = "true", matchIfMissing = false)
@RestController
annotation class ApiMode
