package com.jcaromiq.idempotency

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes


@Aspect
@Component
class IdempotentAspect(val repository: Repository) {

    @Around("isMethodAnnotatedAsIdempotent()")
    fun logAroundMethods(joinPoint: ProceedingJoinPoint): Any? {
        val idempotentId = getIdempotentIdFromHeaders()

        return repository
            .init(idempotentId)
            .orElseGet {
                val data = joinPoint.proceed()
                repository.save(idempotentId, data)
                return@orElseGet data
            }

    }

    private fun getIdempotentIdFromHeaders(): String {
        val request: HttpServletRequest =
            (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader("Idempotency-Key")
    }

    @Pointcut("@annotation(Idempotent)")
    private fun isMethodAnnotatedAsIdempotent() {
    }

}


class RequestInProcessException : Throwable()

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Idempotent
