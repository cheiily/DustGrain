package dustgrain.core.formatting

import kotlinx.serialization.Serializable

@Serializable
enum class FormatterRef(
    var jsonType: String
) {
    WIKITEXT("wikitext"),
    IMAGE("file"),
    PASS("string"),
    PASS_ERROR("")
}