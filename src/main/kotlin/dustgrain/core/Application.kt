package dustgrain.core

import dustgrain.core.cache.CacheMode
import dustgrain.core.cache.DataHeaderCache
import dustgrain.core.cache.InMemoryDataHeaderCache
import dustgrain.core.cache.NoopDataHeaderCache
import dustgrain.core.cache.PersistentDataHeaderCache
import dustgrain.core.config.AppConfig
import dustgrain.core.config.AppProfile
import dustgrain.core.config.getHttpClient
import dustgrain.core.config.loadConfig
import dustgrain.core.fetching.DataFetchService
import io.ktor.client.HttpClient

object Application {
    lateinit var appName: String private set
    lateinit var profile: AppProfile private set
    lateinit var cacheMode: CacheMode private set

    lateinit var config: AppConfig private set
    lateinit var httpClient: HttpClient private set
    lateinit var dataFetchService: DataFetchService private set
    lateinit var dataHeaderCache: DataHeaderCache private set

    fun initialize(profile: AppProfile, applicationName: String, cacheMode: CacheMode) {
        this.appName = applicationName
        this.profile = profile
        this.cacheMode = cacheMode

        this.config = loadConfig()
        this.httpClient = getHttpClient()
        this.dataFetchService = DataFetchService()
        this.dataHeaderCache = when (cacheMode) {
            CacheMode.IN_MEMORY -> InMemoryDataHeaderCache(dataFetchService)
            CacheMode.PERSISTENT -> PersistentDataHeaderCache(dataFetchService)
            CacheMode.NOOP -> NoopDataHeaderCache(dataFetchService)
        }
    }
}