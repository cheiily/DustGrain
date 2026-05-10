package dustgrain.core

import com.github.tomakehurst.wiremock.client.WireMock.*
import dustgrain.core.cache.CacheMode
import dustgrain.core.cache.InMemoryDataHeaderCache
import dustgrain.core.config.AppProfile
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.request.*
import io.ktor.client.request.get as clientGet

class ApplicationMockTest : ApiMockTest({
    feature("Application initialization from config") {
        scenario("should propagate copied config values to initialized components") {
            // given
            val modifiedConfig = mockConfig.copy(
                appInfo = mockConfig.appInfo.copy(name = "renamed-app"),
                cache = mockConfig.cache.copy(mode = CacheMode.IN_MEMORY, maxAgeSeconds = 42L),
                client = mockConfig.client.copy(userAgent = "custom-agent")
            )
            wiremockServer.stubFor(
                get(urlPathMatching("/.*"))
                    .withQueryParam("action", equalTo("ping"))
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody("{}")
                    )
            )

            // when
            Application.initialize(
                profile = AppProfile.CLI,
                appConfig = modifiedConfig
            )
            Application.httpClient.clientGet {
                parameter("action", "ping")
            }

            // then
            Application.config shouldBeEqual modifiedConfig
            Application.dataHeaderCache.shouldBeInstanceOf<InMemoryDataHeaderCache>()
            (Application.dataHeaderCache as InMemoryDataHeaderCache).maxAgeSeconds shouldBeEqual 42L
            wiremockServer.verify(
                getRequestedFor(urlPathMatching("/.*"))
                    .withHeader("User-Agent", equalTo("renamed-app (CLI) custom-agent"))
            )
        }
    }
})
