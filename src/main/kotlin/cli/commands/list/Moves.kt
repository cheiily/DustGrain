package cli.commands.list

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util.listMoves
import java.util.*

class Moves : CommonArgs("List moves for the specified character") {
    val table by option(help = "Table/category to narrow down the list to.")

    override fun run() {
        val tables = Model.scrapeTables(wiki, character)

        val moves =
            if (table == null) Model.listMoves(wiki, character)
            else {
                if (!tables.containsKey(table)) {
                    echo("Invalid argument: No such table found.", err = true)
                    throw ProgramResult(1)
                }
                tables.listMoves(table!!)
            }

        val head = table ?: "All Moves"

        if (pretty) echo(
            "${head.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString() 
            }}: $moves")
        else echo(Json.encodeToString(mapOf(head to moves)))
    }

}