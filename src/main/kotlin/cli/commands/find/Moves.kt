package cli.commands.find

import cli.commands.CommonArgs
import cli.commands.find.filtering.FilterData
import cli.commands.find.filtering.FloatFilter
import cli.commands.find.filtering.StringFilter
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.*
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util


class Moves : CommonArgs("Filter moves by stat.") {
    val stat by argument(help = "Stat column to filter by")
    val table by option("-t", "--table", help = "Limit the search to this table.")
    val filter by option().groupSwitch(mapOf(
        "--num" to FloatFilter(),
        "--text" to StringFilter()
    ))
    val heads by option("--headers", "--headers-only", help = "Only print a list of row-headers (move names). Otherwise list of pairs to filtered value.").flag()

    override fun run() {
        var tables = Model.scrapeTables(wiki, character)
        if (table != null) {
            if (!tables.containsKey(table)) {
                echo("Invalid argument: No such data table found.", err = true)
                throw ProgramResult(1)
            }

            tables = mapOf(table!! to tables[table]!!)
        }

        val data_heads: MutableMap<String, MutableList<String>> = mutableMapOf()
        val data_vals: MutableMap<String, MutableList<String>> = mutableMapOf()

        val filters: MutableList<FilterData> = mutableListOf()

        if (filter is FloatFilter) {
            val f = filter as FloatFilter
            if (f.lt != null) filters += f.lt!!
            if (f.le != null) filters += f.le!!
            if (f.eq != null) filters += f.eq!!
            if (f.gt != null) filters += f.gt!!
            if (f.ge != null) filters += f.ge!!
        } else if (filter is StringFilter) {
            val f = filter as StringFilter
            if (f.eqs != null) filters += f.eqs!!
            if (f.cont != null) filters += f.cont!!
            if (f.start != null) filters += f.start!!
            if (f.end != null) filters += f.end!!
            if (f.reg != null) filters += f.reg!!
        }


        for ((tab, value) in tables.entries) {
            val idx = Util.getHeaders(value).indexOf(stat)
            if (idx == -1) {
                continue
            }
            val temp_heads : MutableList<String> = mutableListOf()
            val temp_vals : MutableList<String> = mutableListOf()

            Util.rows(value)
                .map { Pair(it[0], it[idx]) }
                .filter { it.second.isNotEmpty() }
                .filter {
                    if (filter == null) return@filter true
                    if (filters.isEmpty()) return@filter true

                    val cell = it.second
                    var pass = true
                    for (filter in filters) {
                        if (!filter.filter.matchType(cell)) return@filter false

                        pass = pass and when (filter.filter) {
                            FilterData.Filter.GREATER_THAN -> (it.second.toFloat() > filter.float)
                            FilterData.Filter.GREATER_OR_EQUAL -> (it.second.toFloat() >= filter.float)
                            FilterData.Filter.LESS_THAN -> (it.second.toFloat() < filter.float)
                            FilterData.Filter.LESS_OR_EQUAL -> (it.second.toFloat() <= filter.float)
                            FilterData.Filter.EQUAL_TO -> (it.second.toFloat() == filter.float)
                            FilterData.Filter.EQUAL_TO_STR -> it.second.contentEquals(filter.string, true)
                            FilterData.Filter.CONTAINS -> it.second.contains(filter.string, true)
                            FilterData.Filter.STARTS_WITH -> it.second.startsWith(filter.string, true)
                            FilterData.Filter.ENDS_WITH -> it.second.endsWith(filter.string, true)
                            FilterData.Filter.REGEX -> it.second.matches(filter.string.toRegex())
                        }
                    }

                    return@filter pass
                }
                .forEach {
                    temp_heads += it.first
                    temp_vals += it.second
                }

            data_heads[tab] = temp_heads
            data_vals[tab] = temp_vals
        }

        if (heads) echo(Json.encodeToString(data_heads))
        else {
            val data_zip: MutableMap<String, List<Pair<String, String>>> = mutableMapOf()
            for (key in data_vals.keys) {
                data_zip[key] = data_heads[key]!!.zip(data_vals[key]!!)
            }

            echo(data_zip)
        }
    }

}