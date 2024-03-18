package cli.commands.list

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util
import kotlin.collections.List

class Stat : CommonArgs("Lists the specified stat for every move.") {
    val stat by argument(help = "Stat to list.")
    val table by option(help = "Table to narrow down the list to.")

    override fun run() {
        val datatable = Model.scrapeTables(wiki, character)
        if (table != null) {
            datatable[table]?.let {
                val names = Util.getCol(it, 0)
                val data = Util.getCol(it, stat)
                echo(names.zip(data))
                return
            } ?: run {
                echo("Invalid argument: No such table found.", err = true)
                throw ProgramResult(1)
            }
        } else {
            val ret : MutableMap<String, List<Pair<String, String>>> = mutableMapOf()
            datatable.forEach { (k, v) ->
                    ret[k] = Util.getCol(v, 0).zip(Util.getCol(v, stat))
            }

            echo(ret)
        }
    }
}