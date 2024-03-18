package cli.commands.find.filtering

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option

class StringFilter : OptionGroup("Filter", "Optional filter to apply. The default filter will remove all null/empty fields.") {
    val eqs by option("-eqs", help = "Equal to (string)").convert { v -> FilterData(FilterData.Filter.EQUAL_TO_STR, string = v) }
    val cont by option("-l", "--cont", help = "Contains, \"like\"").convert { v -> FilterData(FilterData.Filter.CONTAINS, string = v) }
    val start by option("-s", "--start", help = "Starts with").convert { v -> FilterData(FilterData.Filter.STARTS_WITH, string = v) }
    val end by option("-e", "--end", help = "Ends with").convert { v -> FilterData(FilterData.Filter.ENDS_WITH, string = v) }
    val reg by option("-r", "--regex", help = "Match to regex").convert { v -> FilterData(FilterData.Filter.REGEX, string = v) }
}