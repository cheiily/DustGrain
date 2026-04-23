package dustgrain.core.domain

import dustgrain.core.formatting.FormatterRef
import kotlinx.serialization.Serializable

@Serializable
data class DataHeader(
    val name: String,
    val type: FormatterRef,
    val delimiter: String?
)
