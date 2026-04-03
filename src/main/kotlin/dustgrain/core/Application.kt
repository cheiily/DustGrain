package dustgrain.core

import dustgrain.core.config.AppConfig
import dustgrain.core.config.AppProfile
import dustgrain.core.config.getHttpClient
import dustgrain.core.config.loadConfig
import dustgrain.core.fetching.DataFetchService
import io.ktor.client.HttpClient

object Application {
    lateinit var appName: String private set
    lateinit var profile: AppProfile private set

    lateinit var config: AppConfig private set
    lateinit var httpClient: HttpClient private set
    lateinit var dataFetchService: DataFetchService private set

    fun initialize(profile: AppProfile, applicationName: String) {
        this.appName = applicationName
        this.profile = profile

        this.config = loadConfig()
        this.httpClient = getHttpClient()
        this.dataFetchService = DataFetchService()
    }
}