package cli.commands.wikitable

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util

class Headers : CommonArgs("Get headers for the specified wikitable.") {
    val table by argument(help = "Table to describe.")
    val vert by option("-v", "--vertical", help = "Retrieve vertical headers.").flag()
    val hori by option("-h", "--horizontal", help = "Retrieve horizontal headers.").flag()

    override fun run() {
        val wiki = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE)

        if ( (vert && hori) || (!vert && !hori) ) {

            wiki[table]?.let {
                val headers = Util.getHeadersWikitable(it)

                if (pretty) echo(
                    "Horizontal:\n- "
                    + headers.first.joinToString("\n- ")
                    + "\nVertical:\n- "
                    + headers.second.joinToString("\n- ")
                )
                else echo(
                    Json.encodeToString(mapOf(Pair("Horizontal", headers.first), Pair("Vertical", headers.second)))
                )

            } ?: run {
                echo("Invalid argument: No such cross-table found.", err = true)
                throw ProgramResult(1)
            }

        } else {
            if (hori) {

            } else if (vert) {

            }

        }

    }
}