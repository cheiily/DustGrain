package dustgrain.core.fetching

import kotlinx.serialization.Serializable

@Serializable
data class DustloopErrorResponse(
    val error: Error,
) {
    @Serializable data class Error(
        val code: String,
        val info: String?,
        val errorclass: String?,
        val `*`: String?
    )
}



@Serializable
data class TableListResponse (
    val cargotables: List<String>
)

@Serializable
data class TableHeaderResponse (
    val cargofields: Map<CargoField, CargoFieldData>
) {
    @Serializable @JvmInline value class CargoField(val name: String)
    @Serializable data class CargoFieldData(
        val type: String,
        val size: String?,
        val isList: String?,
        val delimiter: String?
    )
}

@Serializable
data class TableDataRequest(
    val tables: List<String>,
    val fields: List<String>,
    val where: String? = null,
    val joinOn: String? = null,
    val groupBy: String? = null,
    val having: String? = null,
    val orderBy: String? = null,
    val limit: Int? = null,
    val offset: Int? = null
)


@Serializable
data class TableDataResponse(
    val cargoquery: List<CargoQueryResult>
) {
    @Serializable data class CargoQueryResult(
        val title: Map<String, String?>
    )
}

@Serializable
data class ImageDataResponse(
    val query: ImageUrlQuery
) {
    @Serializable
    data class ImageUrlQuery(
        val pages: List<ImagePage>
    ) {
        @Serializable
        data class ImagePage(
            val imagerepository: String,
            val imageinfo: List<ImageInfo>
        ) {
            @Serializable
            data class ImageInfo(
                val url: String,
                val descriptionurl: String,
                val descriptionshorturl: String,
                val mime: String,
                val size: Int,
                val width: Int,
                val height: Int
            )
        }
    }
}