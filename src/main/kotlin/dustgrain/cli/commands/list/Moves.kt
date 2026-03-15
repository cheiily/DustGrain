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
        Model.timeout = timeout

        val ret = Model.mapOfMoves(wiki, character, table)

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