package com.jcaromiq.idempotency

import org.springframework.stereotype.Component
import java.util.*
import java.util.Optional.empty

interface Repository {
    fun init(idempotent:String): Optional<Any>
    fun save(idempotentId: String, data: Any)
}

@Component
class InMemoryRepository:Repository {
    val repo: MutableMap<String, Optional<Any>> = mutableMapOf()

    override fun init(idempotent: String): Optional<Any> {
        if (repo.containsKey(idempotent) ) {
            if(repo[idempotent]?.isEmpty == true) throw RequestInProcessException()
        }
        else {
            repo[idempotent] = empty()
        }
        return repo[idempotent]!!
    }

    override fun save(idempotentId: String, data: Any) {
        repo[idempotentId] = Optional.of(data)
    }

}