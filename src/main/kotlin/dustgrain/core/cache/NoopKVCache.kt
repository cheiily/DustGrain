package dustgrain.core.cache

import kotlinx.coroutines.runBlocking

open class NoopKVCache<K, V>(
    override val provider: SuspendingCacheEntryProvider<K, V>
) : SuspendingKVCache<K, V> {
    override fun get(key: K): V? = runBlocking {
        provider.get(key)
    }

    override fun set(key: K, value: V) {}

    override fun invalidate(key: K) {}

    override fun clear() {}
}