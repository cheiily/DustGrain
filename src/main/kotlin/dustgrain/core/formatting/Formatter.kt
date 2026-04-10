package dustgrain.core.formatting

import dustgrain.core.domain.DataField
import dustgrain.core.domain.DataGrain
import dustgrain.core.domain.DataHeader

@FunctionalInterface
fun interface Formatter {
    fun format(data: Pair<DataField, DataHeader>): DataGrain
}