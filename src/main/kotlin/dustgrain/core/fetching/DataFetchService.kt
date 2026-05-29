package dustgrain.core.fetching

import dustgrain.core.Application
import dustgrain.core.config.AppConfig
import dustgrain.core.domain.DataHeader
import dustgrain.core.formatting.FormatterRef
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DataFetchService(
    var client: DustloopClient = DustloopClient(Application.httpClient)
) {
    suspend fun getTableList(): List<String> = client.getTableList().cargotables

    suspend fun getTableHeaders(tableName: String): List<DataHeader> = client
        .getTableHeaders(tableName)
        .cargofields
        .map { (field, fieldData) -> DataHeader(
            name = field.name,
            type = FormatterRef.entries
                .firstOrNull { enum ->
                    fieldData.type.contains(enum.jsonType, ignoreCase = true)
                } ?: FormatterRef.PASS_ERROR,
            delimiter = fieldData.delimiter
        ) }

    suspend fun getTableData(query: TableDataRequest): List<Map<String, String?>> = client
        .getTableData(query)
        .cargoquery.map { it.data }

    suspend fun getImageInfo(imageName: String): ImageDataResponse.ImageUrlQuery.ImagePage.ImageInfo = client
        .getImageData(imageName)
        .query.pages.first()
        .imageinfo.first()

    suspend fun getImageUrl(imageName: String): String = client
        .getImageData(imageName)
        .query.pages.first()
        .imageinfo.first()
        .url
}