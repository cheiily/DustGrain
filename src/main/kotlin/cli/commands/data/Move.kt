package cli.commands.data

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util

class Move : CommonArgs("Get a move's data") {

    private val move by argument(help = "Character's move by input")
    val stat by option("-s", "--stat", help = "Specifc stat value (column) to peek.")

    /**
     * Switches on the pretty flag for output formatting.
     */
    override fun run() {
        if (stat != null) {
            var value : String? = null
            val data = Model.scrapeTables(wiki, character)

            for ( (_, v) in data.entries ) {
                if (Util.getCol(v, 0).contains(move)) {
                    value = Util.getStat(v, move, stat!!)
                    break
                }
            }

            if (value == null) {
                echo("Invalid argument: No such stat or move found.", err = true)
                throw ProgramResult(1)
            }

            if (pretty) echo("$stat = $value")
            else echo(Json.encodeToString(mapOf(Pair(stat, value))))

        } else {
            val data = Model.getData(wiki, character, move)

            if (pretty) echo(
                data.toList().joinToString("\n") { pair ->
                    "${pair.first} = ${pair.second}"
                }
            )
            else echo(Json.encodeToString(mapOf("$move" to data)))
        }
    }
}