package dustgrain.core.config

import dustgrain.core.Application
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.util.appendIfNameAbsent

fun getClient() = HttpClient(CIO) {
    this.expectSuccess = true

    install(UserAgent) {
        agent = Application.config.client.userAgent
    }
    install(HttpTimeout) {
        requestTimeoutMillis = Application.config.client.timeout
    }

    defaultRequest {
        url(Application.config.client.url.toString())
        headers.appendIfNameAbsent("format", "json")
        headers.appendIfNameAbsent("Accept", "application/json")
        headers.appendIfNameAbsent(
            "Connection",
            when (Application.profile) {
                AppProfile.CLI -> "close"
                AppProfile.LIB -> "keep-alive"
            }
        )
    }
}