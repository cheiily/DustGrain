package dustgrain.core

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import dustgrain.core.config.AppConfig
import dustgrain.core.config.AppProfile
import dustgrain.core.config.getClient
import dustgrain.core.fetching.TableDataRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.extensions.wiremock.ListenerMode
import io.kotest.extensions.wiremock.WireMockListener
import io.ktor.client.plugins.*
import java.net.URI

abstract class BaseMockTest(body: BaseMockTest.() -> Unit = {}) : FeatureSpec() {
    val logger = KotlinLogging.logger {}
    val wiremockServer: WireMockServer = WireMockServer(WireMockConfiguration.options().port(12345))

    init {
        extension(WireMockListener(
            wiremockServer,
            ListenerMode.PER_TEST
        ))

        body()
    }

    val mockUrl by lazy { wiremockServer.baseUrl() }

    val mockConfig by lazy {
        AppConfig(
            client = AppConfig.Client(URI.create(mockUrl).toURL(), 1000L, "test-agent"),
            cargoQueries = emptyList()
        )
    }

    val mockClient by lazy {
        getClient(
            appName = "",
            appProfile = AppProfile.CLI,
            config = mockConfig
        ).config {
            defaultRequest {
                url("$mockUrl?format=json")
            }
        }
    }

    override suspend fun beforeSpec(spec: Spec) {
        wiremockServer.start()
    }

    override suspend fun afterSpec(spec: Spec) {
        wiremockServer.stop()
    }

    fun thereAreCargoTables() {
        val resourcePath = "/dustgrain/core/fetching/cargotables.json"
        val content = javaClass.getResource(resourcePath)?.readText()
            ?: throw RuntimeException("Resource not found: $resourcePath")

        wiremockServer.stubFor(
            get(urlPathMatching("/.*"))
                .withQueryParam("action", equalTo("cargotables"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(content)
                )
        )
    }

    fun thereAreCargoFields(tableName: String) {
        val resourcePath = "/dustgrain/core/fetching/cargofields_$tableName.json"
        val content = javaClass.getResource(resourcePath)?.readText()
            ?: throw RuntimeException("Resource not found: $resourcePath")

        wiremockServer.stubFor(
            get(urlPathMatching("/.*"))
                .withQueryParam("action", equalTo("cargofields"))
                .withQueryParam("table", equalTo(tableName))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(content)
                )
        )
    }

    fun thereIsAMediaWikiError(variant: String) {
        val resourcePath = "/dustgrain/core/fetching/error_response_$variant.json"
        val content = javaClass.getResource(resourcePath)?.readText()
            ?: throw RuntimeException("Resource not found: $resourcePath")

        wiremockServer.stubFor(
            get(urlPathMatching("/.*"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(content)
                )
        )
    }

    fun thereIsCargoQueryResult(variant: String) {
        val resourcePath = "/dustgrain/core/fetching/cargoquery_$variant.json"
        val content = javaClass.getResource(resourcePath)?.readText()
            ?: throw RuntimeException("Resource not found: $resourcePath")

        wiremockServer.stubFor(
            get(urlPathMatching("/.*"))
                .withQueryParam("action", equalTo("cargoquery"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(content)
                )
        )
    }
}
