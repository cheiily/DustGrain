package dustgrain.core.fetching

import dustgrain.core.Application
import dustgrain.core.config.AppConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DataFetchService(
    var client: HttpClient = Application.httpClient,
    var config: AppConfig = Application.config
) {
    suspend fun getTableList(): List<String> = client.get {
        parameter("action", "cargotables")
    }.body<TableListResponse>().cargotables
}