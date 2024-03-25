package cli.commands.list

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util

class Stat : CommonArgs("Lists the specified stat for every move.") {
    val stat by argument(help = "Stat to list.")
    val table by option("-t", "--table", help = "Table to narrow down the list to.")

    override fun run() {
        val datatable = Model.scrapeTables(wiki, character)
        if (table != null) {
            datatable[table]?.let {
                val names = Util.getCol(it, 0)
                val data = Util.getCol(it, stat)

                val ret = names.zip(data)
                if (pretty)
                    echo(ret.joinToString("\n") { pair -> "${pair.first} : ${pair.second}" })
                else
                    echo(Json.encodeToString(mapOf(stat to ret.toMap())))

            } ?: run {
                echo("Invalid argument: No such table found.", err = true)
                throw ProgramResult(1)
            }
        } else {
            val ret: MutableMap<String, Map<String, String>> = mutableMapOf()
            datatable.forEach { (k, v) ->
                ret[k] = Util.getCol(v, 0).zip(Util.getCol(v, stat)).toMap()
            }

            if (pretty) {
                echo( ret.entries.joinToString("\n") {
                    entry -> "${entry.key}:\n\t" + entry.value.entries.joinToString("\n\t") {
                        moveEntry -> "${moveEntry.key} : ${moveEntry.value}"
                    }
                } )
            } else
                echo(Json.encodeToString(ret))
        }
    }
}