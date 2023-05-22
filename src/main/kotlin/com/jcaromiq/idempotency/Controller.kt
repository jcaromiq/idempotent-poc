package com.jcaromiq.idempotency

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class Controller {

    @PostMapping("/pay")
    @Idempotent
    fun pay(
        @RequestBody paymentRequest: PaymentRequest,
    ): ResponseEntity<PayResponse> {
        Thread.sleep(2000)
        return ResponseEntity.ok(PayResponse(paymentRequest.invoiceId, "OK"))
    }
}

data class PaymentRequest(val invoiceId:String, val amount:Double)

data class PayResponse(val id: String, val status: String)

@ControllerAdvice
class Advice {
    @ExceptionHandler(value = [RequestInProcessException::class])
    fun requestInProcessException(exception: RequestInProcessException): ResponseEntity<String> {
        return ResponseEntity.accepted().build()
    }
}
