package dustgrain.core.cache

open class InMemoryKVCache<K, V>(
    override val provider: SuspendingCacheEntryProvider<K, V>
) : SuspendingKVCache<K, V> {
    private val cache = mutableMapOf<K, V>()

    override fun get(key: K): V? = cache[key]

    override fun set(key: K, value: V) {
        cache[key] = value
    }

    override fun invalidate(key: K) {
        cache.remove(key)
    }

    override fun clear() {
        cache.clear()
    }
}