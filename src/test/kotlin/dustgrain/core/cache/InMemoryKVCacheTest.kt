package dustgrain.core.cache

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class InMemoryKVCacheTest : FeatureSpec({

    fun someProvider(): SuspendingCacheEntryProvider<String, String> {
        return SuspendingCacheEntryProvider { key -> "loaded value for $key" }
    }

    feature("InMemoryKVCache#get") {
        scenario("should return null for a key that has not been set") {
            // given
            val cache = InMemoryKVCache(someProvider(), 60)

            // when
            val value = cache.get("key1")

            // then
            value.shouldBeNull()
        }

        scenario("should return a value that has been set") {
            // given
            val cache = InMemoryKVCache(someProvider(), 60)
            cache.set("key1", "value1")

            // when
            val value = cache.get("key1")

            // then
            value shouldBe "value1"
        }
    }

    feature("InMemoryKVCache#set") {
        scenario("should store a value in the cache") {
            // given
            val cache = InMemoryKVCache(someProvider(), 60)

            // when
            cache.set("key1", "value1")

            // then
            cache.get("key1") shouldBe "value1"
        }
    }

    feature("InMemoryKVCache#invalidate") {
        scenario("should remove a value from the cache") {
            // given
            val cache = InMemoryKVCache(someProvider(), 60)
            cache.set("key1", "value1")

            // when
            cache.invalidate("key1")

            // then
            cache.get("key1").shouldBeNull()
        }
    }

    feature("InMemoryKVCache#clear") {
        scenario("should remove all values from the cache") {
            // given
            val cache = InMemoryKVCache(someProvider(), 60)
            cache.set("key1", "value1")
            cache.set("key2", "value2")

            // when
            cache.clear()

            // then
            cache.get("key1").shouldBeNull()
            cache.get("key2").shouldBeNull()
        }
    }
})

