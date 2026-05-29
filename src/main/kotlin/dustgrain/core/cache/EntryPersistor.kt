package dustgrain.core.cache

import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.io.path.exists
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.readText
import kotlin.io.path.writeText

class PersistResult(
    val key: String,
    val content: String?,
    val success: Boolean,
    val error: Throwable? = null,
    val age : Long? = null
) {
    fun getOrThrow(): String? {
        if (error != null) throw error
        if (!success) throw IllegalStateException("Persist operation failed silently for key {$key} (no file found).")
        return content
    }
    
    companion object {
        fun success(key: String, content: String?) = PersistResult(key, content, true)
        fun success(key: String, content: String?, age: Long) = PersistResult(key, content, true, null, age)
        fun failure(key: String, error: Throwable) = PersistResult(key, null, false, error)
        fun notFound(key: String) = PersistResult(key, null, false)
    }
}

class EntryPersistor(
    private val directory: Path,
    private val version: Int
) {
    private var directoryExists: Boolean = false
    private val isEmpty get() = directory.toFile().listFiles()?.isEmpty() == true
    private val path = directory.resolve("v$version")

    private fun prepareDirectory() {
        if (!directoryExists) {
            Files.createDirectories(path)
            directoryExists = true
        }
    }

    private fun cleanDirectory() {
        if (isEmpty && directoryExists) {
            Files.delete(path)
            directoryExists = false
        }
    }
    
    fun put(key: String, content: String) = try {
        prepareDirectory()
        path.resolve(key).let {
            if (!it.exists())
                Files.createFile(it)
            it
        }.writeText(content)
        PersistResult.success(key, content)
    } catch (ex: Throwable) {
        PersistResult.failure(key, ex)
    }

    fun get(key: String): PersistResult = try {
        path.resolve(key)
            .takeIf { it.exists() }
            ?.let {
                PersistResult.success(
                    key = key,
                    content = it.readText(),
                    age = ChronoUnit.SECONDS.between(it.getLastModifiedTime().toInstant(), Instant.now())
                )
            } ?: PersistResult.notFound(key)
    } catch (ex: Throwable) {
        PersistResult.failure(key, ex)
    }
    
    fun remove(key: String) = try {
        path.resolve(key)
            .takeIf { it.exists() }
            ?.let {
                Files.delete(it)
            }
        cleanDirectory()
        PersistResult.success(key, null)
    } catch (ex: Throwable) {
        PersistResult.failure(key, ex)
    }
    
    fun clear() = try {
        if (directoryExists) {
            Files.walk(path)
                .filter { it != path }
                .forEach { Files.delete(it) }
            Files.delete(path)
            directoryExists = false
        }
        PersistResult.success("cache", null)
    } catch (ex: Throwable) {
        PersistResult.failure("cache", ex)
    }
}