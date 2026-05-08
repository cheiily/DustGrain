package dustgrain.core

import dustgrain.core.config.AppConfig
import dustgrain.core.config.AppProfile
import dustgrain.core.config.getHttpClient
import dustgrain.core.fetching.DataFetchService
import dustgrain.core.fetching.DustloopClient
import dustgrain.core.formatting.FormattingService
import io.kotest.core.spec.style.FeatureSpec
import io.ktor.client.plugins.defaultRequest
import java.net.URI

abstract class ComponentMockTest(body: ComponentMockTest.() -> Unit = {}) : FeatureSpec({}) {
    init {
        body()
    }

    open val mockUrl = "http://localhost:12345"

    val mockConfig by lazy {
        AppConfig(
            appInfo = AppConfig.AppInfo(
                name = "test-app",
                version = "0.1.0",
                author = "test-author"
            ),
            cache = AppConfig.Cache(
                version = 1,
                maxAgeSeconds = 3600L
            ),
            client = AppConfig.Client(
                url = URI.create(mockUrl).toURL(),
                timeout = 1000L,
                userAgent = "test-agent"),
            cargoQueries = emptyList()
        )
    }

    val mockClient by lazy {
        getHttpClient(
            appName = "",
            appProfile = AppProfile.CLI,
            config = mockConfig
        ).config {
            defaultRequest {
                url("$mockUrl?format=json")
            }
        }
    }

    val mockDustloopClient by lazy {
        DustloopClient(client = mockClient)
    }

    val mockDataFetchService by lazy {
        DataFetchService(client = mockDustloopClient)
    }

    val mockFormattingService by lazy {
        FormattingService(dataFetchService = mockDataFetchService)
    }
}