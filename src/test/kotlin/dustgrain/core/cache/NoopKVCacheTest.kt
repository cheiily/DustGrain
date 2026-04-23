package dustgrain.core.cache

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay

class NoopKVCacheTest : FeatureSpec({
    fun someProvider(): SuspendingCacheEntryProvider<String, Int> {
        return object : SuspendingCacheEntryProvider<String, Int> {
            var counter = 0
                private set

            override suspend fun get(key: String): Int {
                counter++
                delay(10) // simulate suspension
                return counter
            }
        }
    }

    feature("NoopKVCache#get") {
        scenario("should call provider and return value") {
            // given
            val cache = NoopKVCache(someProvider())

            // when
            val value = cache.get("key1")

            // then
            value shouldBe 1
        }
    }

    feature("NoopKVCache#set") {
        scenario("should not cache") {
            // given
            val provider = someProvider()
            val cache = NoopKVCache(provider)

            // when
            cache.get("key1")
            cache.set("key1", 3)

            // then
            cache.get("key1") shouldBe 2
        }

        scenario("should not throw") {
            // given
            val cache = NoopKVCache(someProvider())

            // when / then
            cache.set("key1", 0)
        }
    }

    feature("NoopKVCache#invalidate") {
        scenario("should not throw") {
            // given
            val cache = NoopKVCache(someProvider())

            // when / then
            cache.invalidate("key1")
        }
    }

    feature("NoopKVCache#clear") {
        scenario("should not throw") {
            // given
            val cache = NoopKVCache(someProvider())

            // when / then
            cache.clear()
        }
    }
})
