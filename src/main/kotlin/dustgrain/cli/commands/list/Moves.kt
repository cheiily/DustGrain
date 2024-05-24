package dustgrain.cli.commands.list

import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.options.option
import dustgrain.cli.commands.CommonArgs
import dustgrain.model.Model
import dustgrain.model.Util
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.collections.List

class Moves : CommonArgs("List moves for the specified character") {
    val table by option("-t", "--table", help = "Table/category to narrow down the list to.")

    override fun run() {
        var tables = Model.scrapeTables(wiki, character)
        if (table != null) {
            if (!tables.containsKey(table)) {
                echo("Invalid argument: No such data table found.", err = true)
                throw ProgramResult(1)
            }

            tables = mapOf(table!! to tables[table]!!)
        }

        var ret: MutableMap<String, List<String>> = mutableMapOf()
        for ((k, v) in tables) {
            ret[k] = Util.getCol(v, "input");
        }

        if (pretty) {
            var str = ""
            for ((k, v) in ret.filter { (_,v) -> v.isNotEmpty() }) {
                str += "$k\n\t- " + v.joinToString("\n\t- ") + '\n'
            }
            echo(str)
        } else
            echo(Json.encodeToString(ret.filter { (_,v) -> v.isNotEmpty() }))
    }

}