package dustgrain.core.cache

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe

class SuspendingKVCacheTest : FeatureSpec({

    class TestCache : SuspendingKVCache<String, String> {
        private val cache = mutableMapOf<String, String>()

        override fun get(key: String): String? = cache[key]

        override fun set(key: String, value: String) {
            cache[key] = value
        }

        override fun invalidate(key: String) {
            cache.remove(key)
        }

        override fun clear() {
            cache.clear()
        }

        override val provider: SuspendingCacheEntryProvider<String, String>
            get() = SuspendingCacheEntryProvider { _ -> "provided" }
    }

    feature("SuspendingKVCache#getOrLoad") {
        scenario("should return cached value if present") {
            // given
            val cache = TestCache()
            cache.set("key1", "cached value")

            // when
            val value = cache.getOrLoad("key1")

            // then
            value shouldBe "cached value"
        }

        scenario("should load and cache value if not present") {
            // given
            val cache = TestCache()

            // when
            val value = cache.getOrLoad("key1")

            // then
            value shouldBe "provided"
            cache.get("key1") shouldBe "provided"
        }
    }

    feature("SuspendingKVCache#getOrLoadBlocking") {
        scenario("should return cached value if present") {
            // given
            val cache = TestCache()
            cache.set("key1", "cached value")

            // when
            val value = cache.getOrLoadBlocking("key1")

            // then
            value shouldBe "cached value"
        }

        scenario("should load and cache value if not present") {
            // given
            val cache = TestCache()

            // when
            val value = cache.getOrLoadBlocking("key1")

            // then
            value shouldBe "provided"
            cache.get("key1") shouldBe "provided"
        }
    }

    feature("SuspendingKVCache#load") {
        scenario("should load and cache value") {
            // given
            val cache = TestCache()

            // when
            val value = cache.load("key1")

            // then
            value shouldBe "provided"
            cache.get("key1") shouldBe "provided"
        }
    }

    feature("SuspendingKVCache#loadBlocking") {
        scenario("should load and cache value") {
            // given
            val cache = TestCache()

            // when
            val value = cache.loadBlocking("key1")

            // then
            value shouldBe "provided"
            cache.get("key1") shouldBe "provided"
        }
    }

    feature("SuspendingKVCache#loadAll") {
        scenario("should load and cache all values") {
            // given
            val cache = TestCache()

            // when
            cache.loadAll("key1", "key2")

            // then
            cache.get("key1") shouldBe "provided"
            cache.get("key2") shouldBe "provided"
        }
    }

    feature("SuspendingKVCache#loadAllBlocking") {
        scenario("should load and cache all values") {
            // given
            val cache = TestCache()

            // when
            cache.loadAllBlocking("key1", "key2")

            // then
            cache.get("key1") shouldBe "provided"
            cache.get("key2") shouldBe "provided"
        }
    }
})
