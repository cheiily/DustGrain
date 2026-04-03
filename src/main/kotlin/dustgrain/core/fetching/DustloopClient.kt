package dustgrain.core.fetching

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DustloopClient(
    val client: HttpClient
) {
    suspend fun getTableList(): TableListResponse = client.get {
        parameter("action", "cargotables")
    }.body()

    suspend fun getTableHeaders(tableName: String): TableHeaderResponse = client.get {
        parameter("action", "cargofields")
        parameter("table", tableName)
    }.body()

    suspend fun getTableData(request: TableDataRequest): TableDataResponse = client.get {
        parameter("action", "cargoquery")
        parameter("tables", request.tables.joinToString(","))
        parameter("fields", request.fields.joinToString(","))
        request.where?.let { parameter("where", it) }
        request.joinOn?.let { parameter("join_on", it) }
        request.groupBy?.let { parameter("group_by", it) }
        request.having?.let { parameter("having", it) }
        request.orderBy?.let { parameter("order_by", it) }
        request.limit?.let { parameter("limit", it) }
        request.offset?.let { parameter("offset", it) }
    }.body()

    suspend fun getImageData(imageName: String): ImageDataResponse = client.get {
        val fileParam = if (imageName.startsWith("File:")) imageName else "File:$imageName"
        parameter("action", "query")
        parameter("prop", "imageinfo")
        parameter("iiprop", "url|size|mime")
        parameter("titles", fileParam)
        parameter("formatversion", 2)
    }.body()
}