package dustgrain.cli.commands.data

import dustgrain.cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dustgrain.model.Model
import dustgrain.model.Util

class System : CommonArgs("Retrieves system data for specified character.") {
    override fun run() {
        Model.timeout = timeout

        val tables = Model.scrapeTables(wiki, character, Model.TableType.CARGO_TABLE)

        tables["System Data"]?.let {
            val ret = Util.getHeaders(it).zip(Util.getRow(it, 0))

            if (pretty) echo(
                ret.toList().joinToString("\n") { pair ->
                    "${pair.first} = ${pair.second}"
                }
            )
            else echo(Json.encodeToString(mapOf("$character" to ret.toMap())))
        } ?: run {
            echo("No \"System Data\" table found for this url.", err = true)
            throw ProgramResult(1)
        }
    }
}