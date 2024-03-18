package cli.commands.find.filtering

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.float

class FloatFilter : OptionGroup("Filter", "Optional filter to apply. The default filter will remove all null/empty fields.") {
    val gt by option("-gt", help = "Greater than").float().convert { v -> FilterData(FilterData.Filter.GREATER_THAN, v) }
    val ge by option("-ge", help = "Greater than or equal to").float().convert { v -> FilterData(FilterData.Filter.GREATER_OR_EQUAL, v) }
    val lt by option("-lt", help = "Less than").float().convert { v -> FilterData(FilterData.Filter.LESS_THAN, v) }
    val le by option("-le", help = "Less than or equal to").float().convert { v -> FilterData(FilterData.Filter.LESS_OR_EQUAL, v) }
    val eq by option("-eq", help = "Equal to").float().convert { v -> FilterData(FilterData.Filter.EQUAL_TO, v) }
}