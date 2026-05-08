package dustgrain.core.cache

import dustgrain.core.Application
import dustgrain.core.ComponentMockTest
import dustgrain.core.config.AppProfile
import io.kotest.matchers.nulls.shouldNotBeNull

class DataHeaderCacheMockTest : ComponentMockTest({
    feature("InMemoryDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService
            Application.initialize(
                profile = AppProfile.CLI,
                appConfig = mockConfig.copy(cache = mockConfig.cache.copy(mode = CacheMode.IN_MEMORY))
            )

            // when
            val cache = InMemoryDataHeaderCache(dataFetchService)

            // then
            cache.shouldNotBeNull()
        }
    }

    feature("PersistentDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService
            Application.initialize(
                profile = AppProfile.CLI,
                appConfig = mockConfig.copy(cache = mockConfig.cache.copy(mode = CacheMode.PERSISTENT))
            )

            // when
            val cache = PersistentDataHeaderCache(dataFetchService)

            // then
            cache.shouldNotBeNull()

            // cleanup
            cache.clear()
        }
    }

    feature("NoopDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService
            Application.initialize(
                profile = AppProfile.CLI,
                appConfig = mockConfig.copy(cache = mockConfig.cache.copy(mode = CacheMode.NOOP))
            )

            // when
            val cache = NoopDataHeaderCache(dataFetchService)

            // then
            cache.shouldNotBeNull()
        }
    }
})
