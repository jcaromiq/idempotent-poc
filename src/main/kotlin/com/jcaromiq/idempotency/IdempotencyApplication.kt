package com.jcaromiq.idempotency

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class IdempotencyApplication

fun main(args: Array<String>) {
	runApplication<IdempotencyApplication>(*args)
}
