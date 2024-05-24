package dustgrain.cli.commands.list

import dustgrain.cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dustgrain.model.Model
import dustgrain.model.Util

class Stats : CommonArgs("List all stats available for this table (headers).") {
    val table by argument(help = "Table to describe.")

    override fun run() {
        val datatable = Model.scrapeTables(wiki, character)
        datatable[table]?.let {
            if (pretty)
                echo(Util.getHeaders(it).joinToString(", "))
            else
                echo(Json.encodeToString(mapOf(table to Util.getHeaders(it))))
        } ?: run {
            echo("Invalid argument: No such table found.", err = true)
            throw ProgramResult(1)
        }

    }

}