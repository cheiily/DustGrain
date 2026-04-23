package dustgrain.core.cache

import dustgrain.core.ComponentMockTest
import dustgrain.core.deleteRecursively
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.nulls.shouldNotBeNull
import net.harawata.appdirs.AppDirsFactory
import okio.Path.Companion.toPath
import java.nio.file.Path

class DataHeaderCacheMockTest : ComponentMockTest({
    feature("InMemoryDataHeaderCache constructor") {
        scenario("should create an instance") {
            // given
            val dataFetchService = mockDataFetchService

            // when
            val cache = InMemoryDataHeaderCache(dataFetchService)

            // then
            cache.shouldNotBeNull()
        }
    }

    feature("PersistentDataHeaderCache constructor") {
        scenario("should create an instance and its directory") {
            // given
            val dataFetchService = mockDataFetchService
            val expectedDir = AppDirsFactory.getInstance().getUserCacheDir(
                mockConfig.appInfo.name,
                mockConfig.appInfo.version + "-c" + mockConfig.appInfo.cacheVersion,
                mockConfig.appInfo.author
            )

            // when
            val cache = PersistentDataHeaderCache(dataFetchService, mockConfig)

            // then
            cache.shouldNotBeNull()
            Path.of(expectedDir).toFile().shouldExist()

            // cleanup
            cache.clear()
            cache.close()
            Path.of(expectedDir).deleteRecursively()
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