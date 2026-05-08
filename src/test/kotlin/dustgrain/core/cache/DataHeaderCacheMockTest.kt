package dustgrain.core.cache

import dustgrain.core.ComponentMockTest
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