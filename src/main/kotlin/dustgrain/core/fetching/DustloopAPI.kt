package dustgrain.core.fetching

import kotlinx.serialization.Serializable

@Serializable
data class TableListResponse (
    val cargotables: List<String>
)