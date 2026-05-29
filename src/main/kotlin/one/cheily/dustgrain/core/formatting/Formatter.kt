package one.cheily.dustgrain.core.formatting

import one.cheily.dustgrain.core.domain.DataField
import one.cheily.dustgrain.core.domain.DataGrain
import one.cheily.dustgrain.core.domain.DataHeader

@FunctionalInterface
fun interface Formatter {
    fun format(data: Pair<DataField, DataHeader>): DataGrain
}