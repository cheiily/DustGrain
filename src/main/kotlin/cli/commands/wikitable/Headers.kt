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
    val vertical by option("-v", "--vertical", help = "Retrieve vertical headers.").flag()
    val horizontal by option("-h", "--horizontal", help = "Retrieve horizontal headers.").flag()

    override fun run() {
        val wiki = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE)

        if (!wiki.containsKey(table)) {
            echo("Invalid argument: No such cross-table found.", err = true)
            throw ProgramResult(1)
        }

        var vert = vertical
        var hori = horizontal
        if (!vertical && !horizontal) {
            vert = true; hori = true
        }

        var ret : MutableMap<String, List<String>> = mutableMapOf()

        if (vert)
            ret["Vertical Headers"] = wiki[table]?.let { Util.getHeadersVerticalWikitable(it) }!!
        if (hori)
            ret["Horizontal Headers"] = wiki[table]?.let { Util.getHeadersHorizontalWikitable(it) }!!

        if (pretty)
            echo(ret.entries.joinToString("\n") {
                    entry -> "${entry.key}:\n\t" + entry.value.joinToString("\n\t") { s: String -> "- $s" }
            })
        else echo(Json.encodeToString(ret))
    }
}