package dustgrain.cli.commands.wikitable

import dustgrain.cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dustgrain.model.Model
import dustgrain.model.Util

class Row : CommonArgs("Extract a row from a cross-table") {
    val table by argument(help = "Table to describe.")
    val method by mutuallyExclusiveOptions(
        option("-i", "--index", help = "Index of desired row.").convert { v -> v.toInt() },
        option("-h", "--header", help = "Header (vertical) of desired row.")
    )

    override fun run() {
        Model.timeout = timeout

        val wiki = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE)
        wiki[table]?.let {
            val ret : List<String> = when (method) {
                is Int -> Util.getRow(it, method as Int, true)
                is String -> Util.getRow(it, method as String)
                else -> {
                    echo("Missing argument: No row specifier.", err = true)
                    throw ProgramResult(1)
                }
            }

            val head = if (method is Int) Util.getHeadersVerticalWikitable(it)[method as Int] else method as String
            if (pretty)
                echo("In row $head are:\n\t" + ret.joinToString("\n\t") { s: String -> "-$s" })
            else echo(Json.encodeToString(mapOf(head to ret)))
        } ?: run {
            echo("Invalid argument: No such cross-table found.", err = true)
            throw ProgramResult(1)
        }
    }

}