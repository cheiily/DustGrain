package dustgrain.core.cache

import dustgrain.core.deleteRecursively
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.io.File

class PersistentKVCacheTest : FeatureSpec({

    val keyCodec = StringCodec()
    val valueCodec = StringCodec()

    fun someProvider(): SuspendingCacheEntryProvider<String, String> {
        return SuspendingCacheEntryProvider { key -> "loaded value for $key" }
    }

    lateinit var tempDir : File
    fun getCache() = PersistentKVCache(tempDir.path, someProvider(), keyCodec, valueCodec)
    lateinit var cache : PersistentKVCache<String, String>

    beforeEach {
        tempDir = tempdir()
        cache = getCache()
    }

    afterEach {
        cache.clear()
        cache.close()
        tempDir.toPath().deleteRecursively()
    }

    feature("PersistentKVCache#get and set") {
        scenario("should return a value that has been set") {
            // given
            cache

            // when
            cache.set("key1", "value1")
            val value = cache.get("key1")

            // then
            value shouldBe "value1"
        }

        scenario("should return null for a key that has not been set") {
            // given
            cache

            // when
            val value = cache.get("key1")

            // then
            value.shouldBeNull()
        }
    }

    feature("PersistentKVCache#invalidate") {
        scenario("should remove a value from the cache") {
            // given
            cache.set("key1", "value1")

            // when
            cache.invalidate("key1")
            val value = cache.get("key1")

            // then
            value.shouldBeNull()
        }
    }

    feature("PersistentKVCache#clear") {
        scenario("should remove all values from the cache") {
            // given
            cache.set("key1", "value1")
            cache.set("key2", "value2")

            // when
            cache.clear()

            // then
            cache.get("key1").shouldBeNull()
            cache.get("key2").shouldBeNull()
        }
    }

    feature("PersistentKVCache#persistence") {
        scenario("should persist data between cache instances") {
            // given
            val cache1 = PersistentKVCache(tempDir.path, someProvider(), keyCodec, valueCodec)
            cache1.set("key1", "persistent value")
            cache1.close()

            // when
            // Create a new cache instance pointing to the same directory
            val cache2 = PersistentKVCache(tempDir.path, someProvider(), keyCodec, valueCodec)
            val value = cache2.get("key1")

            // then
            value shouldBe "persistent value"
            cache2.close()
        }
    }

    feature("PersistentKVCache#getOrLoad") {
        scenario("should return cached value if present") {
            // given
            cache.set("key1", "cached value")

            // when
            val value = cache.getOrLoad("key1")

            // then
            value shouldBe "cached value"
        }

        scenario("should load and cache value if not present") {
            // given
            cache

            // when
            val value = cache.getOrLoad("key1")

            // then
            value shouldBe "loaded value for key1"
            cache.get("key1") shouldBe "loaded value for key1"
        }
    }
})
