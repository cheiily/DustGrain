package dustgrain.core

import dustgrain.core.config.AppConfig
import dustgrain.core.config.AppProfile
import dustgrain.core.config.getClient
import dustgrain.core.config.loadConfig
import io.ktor.client.HttpClient

object Application {
    lateinit var profile: AppProfile private set
    lateinit var config: AppConfig private set
    lateinit var httpClient: HttpClient private set

    fun initialize(profile: AppProfile) {
        this.profile = profile
        this.config = loadConfig()
        this.httpClient = getClient()
    }
}