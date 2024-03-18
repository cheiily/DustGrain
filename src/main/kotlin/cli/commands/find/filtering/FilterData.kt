package cli.commands.find.filtering

data class FilterData(
    val filter : Filter,
    val float: Float = 0.0f,
    val string: String = ""
) {
    enum class Filter {
        GREATER_THAN, GREATER_OR_EQUAL, LESS_THAN, LESS_OR_EQUAL, EQUAL_TO,
        EQUAL_TO_STR, CONTAINS, STARTS_WITH, ENDS_WITH, REGEX;

        fun matchType(value: String?): Boolean {
            if (value == null) return false
            return when (this) {
                GREATER_THAN, GREATER_OR_EQUAL, LESS_THAN, LESS_OR_EQUAL, EQUAL_TO -> value.toFloatOrNull() != null
                EQUAL_TO_STR, CONTAINS, STARTS_WITH, ENDS_WITH, REGEX -> true
            }
        }
    }
}