package dustgrain.core.formatting

enum class FormatterRef(
    var jsonType: String
) {
    WIKITEXT("wikitext"),
    IMAGE("file"),
    PASS("string"),
    PASS_ERROR("")
}