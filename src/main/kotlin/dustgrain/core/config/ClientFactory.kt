package dustgrain.core.config

import dustgrain.core.Application
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*

fun getClient() = HttpClient(CIO) {
    this.expectSuccess = true

    install(UserAgent) {
        agent = "${Application.appName} ${Application.config.client.userAgent}".trim()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = Application.config.client.timeout
    }
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 3)
    }
    install(ContentNegotiation) {
        json()
    }

    defaultRequest {
        url(Application.config.client.url.toString() + "?format=json")
        headers.appendIfNameAbsent("Accept", "application/json")
        headers.appendIfNameAbsent("Accept-Charset", "utf-8")
        headers.appendIfNameAbsent(
            "Connection",
            when (Application.profile) {
                AppProfile.CLI -> "close"
                AppProfile.LIB -> "keep-alive"
            }
        )
    }
}