package dustgrain.core.cache

import dustgrain.core.ComponentMockTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldNotBeNull

class DataHeaderCacheMockTest : ComponentMockTest({
    feature("InMemoryDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService

            // when
            val cache = InMemoryDataHeaderCache(dataFetchService, mockConfig)

            // then
            cache.shouldNotBeNull()
        }

        scenario("should use maxAgeSeconds override when provided") {
            // given
            val dataFetchService = mockDataFetchService
            val override = 42L

            // when
            val cache = InMemoryDataHeaderCache(dataFetchService, mockConfig, override)

            // then
            cache.maxAgeSeconds shouldBe override
        }
    }

    feature("PersistentDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService

            // when
            val cache = PersistentDataHeaderCache(dataFetchService, mockConfig)

            // then
            cache.shouldNotBeNull()

            // cleanup
            cache.clear()
        }

        scenario("should use maxAgeSeconds override when provided") {
            // given
            val dataFetchService = mockDataFetchService
            val override = 42L

            // when
            val cache = PersistentDataHeaderCache(dataFetchService, mockConfig, override)

            // then
            cache.maxAgeSeconds shouldBe override

            // cleanup
            cache.clear()
        }
    }

    feature("NoopDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService

            // when
            val cache = NoopDataHeaderCache(dataFetchService)

            // then
            cache.shouldNotBeNull()
        }
    }
})
