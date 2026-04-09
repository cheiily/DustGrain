package dustgrain.core.domain

import dustgrain.core.formatting.FormatterRef

data class DataHeader(
    val name: String,
    val type: FormatterRef,
    val delimiter: String?
)
