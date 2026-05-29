package dustgrain.core.cache

import java.time.Instant
import java.time.temporal.ChronoUnit

open class InMemoryKVCache<K, V>(
    override val provider: SuspendingCacheEntryProvider<K, V>,
    val maxAgeSeconds: Long
) : SuspendingKVCache<K, V> {
    private val cache = mutableMapOf<K, V>()
    private val cacheAge = mutableMapOf<K, Instant>()

    override fun get(key: K): V? = cache[key]?.let { v ->
        cacheAge[key]?.let {
            if (ChronoUnit.SECONDS.between(it, Instant.now()) > maxAgeSeconds) {
                invalidate(key)
                null
            } else {
                v
            }
        }
    }

    override fun set(key: K, value: V) {
        cache[key] = value
        cacheAge[key] = Instant.now()
    }

    override fun invalidate(key: K) {
        cache.remove(key)
    }

    override fun clear() {
        cache.clear()
    }
}