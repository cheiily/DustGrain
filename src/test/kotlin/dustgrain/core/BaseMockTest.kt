package dustgrain.core

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import dustgrain.core.config.AppConfig
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FeatureSpec
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import java.net.URI

abstract class BaseMockTest(body: BaseMockTest.() -> Unit = {}) : FeatureSpec() {
    val server = WireMockServer(WireMockConfiguration.options().port(12345))

    val mockUrl by lazy { server.baseUrl() }

    val mockClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            defaultRequest {
                url(mockUrl)
            }
        }
    }

    val mockConfig by lazy {
        AppConfig(
            client = AppConfig.Client(URI.create(mockUrl).toURL(), 1000L, "test-agent"),
            cargoQueries = emptyList()
        )
    }

    init {
        body()
    }

    override suspend fun beforeSpec(spec: Spec) {
        server.start()
    }

    override suspend fun afterSpec(spec: Spec) {
        server.stop()
    }

    fun thereAreCargoTables() {
        val resourcePath = "/dustgrain/core/fetching/cargotables.json"
        val content = object {}.javaClass.getResource(resourcePath)?.readText()
            ?: throw RuntimeException("Resource not found: $resourcePath")

        server.stubFor(
            get(urlPathMatching("/.*"))
                .withQueryParam("action", equalTo("cargotables"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(content)
                )
        )
    }
}
