package dustgrain.core.cache

import com.mayakapps.kache.FileKache
import com.mayakapps.kache.KacheStrategy
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import java.nio.file.Files
import java.nio.file.Path

open class PersistentKVCache<K, V>(
    val directory: String,
    override val provider: SuspendingCacheEntryProvider<K, V>,
    val keyCodec: Codec<K, String>,
    val valueCodec: Codec<V, String>
) : SuspendingKVCache<K, V> {
    private val logger = KotlinLogging.logger {}

    private val cache = runBlocking {
        FileKache(directory, maxSize = 100L * 1024) { // 100 KB
            strategy = KacheStrategy.LRU
            cacheVersion = 1
        }
    }

    fun close() = runBlocking { cache.close() }

    override fun get(key: K): V? = runBlocking {
        keyCodec.encoder.encode(key)
        cache.get(keyCodec.encode(key))?.let {
            try {
                Files.readString(Path.of(it))
            } catch (ex: IOException) {
                logger.error { "Couldn't read cache: ${ex.message}" }
                null
            }
        }?.let {
            valueCodec.decode(it)
        }
    }

    override fun set(key: K, value: V) {
        runBlocking {
            cache.put(keyCodec.encode(key)) { file ->
                val serialized = valueCodec.encode(value)
                try {
                    Files.writeString(Path.of(file), serialized)
                    true
                } catch (ex: IOException) {
                    logger.error { "Couldn't save cache: ${ex.message}" }
                    false
                }
            }
        }
    }

    override fun invalidate(key: K) = runBlocking {
        cache.remove(keyCodec.encode(key))
    }

    override fun clear() = runBlocking {
        cache.clear()
    }
}