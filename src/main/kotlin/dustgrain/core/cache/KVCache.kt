package dustgrain.core.cache

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun interface SuspendingCacheEntryProvider<K, V> {
    suspend fun get(key: K): V?
}

interface SuspendingKVCache<K, V> {
    fun get(key: K): V?
    fun set(key: K, value: V)
    fun invalidate(key: K)
    fun clear()

    suspend fun getOrLoad(key: K): V? = get(key) ?: load(key)
    fun getOrLoadBlocking(key: K): V? = get(key) ?: loadBlocking(key)

    val provider: SuspendingCacheEntryProvider<K, V>
    suspend fun load(key: K): V? = provider.get(key)?.let {
        set(key, it)
        it
    }
    fun loadBlocking(key: K): V? = runBlocking { provider.get(key)?.let {
        set(key, it)
        it
    }}
    suspend fun loadAll(vararg keys: K) = keys.forEach { load(it) }
    fun loadAllBlocking(vararg keys: K) = runBlocking {
        keys.map { async { load(it) } }
            .awaitAll()
    }
}