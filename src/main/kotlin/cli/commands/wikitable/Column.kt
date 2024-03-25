package cli.commands.wikitable

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import model.Util

class Column : CommonArgs("Extract a column from a cross-table") {
    val table by argument(help = "Table to describe.")
    val method by mutuallyExclusiveOptions(
        option("-i", "--index", help = "Index of desired column").convert { v -> v.toInt() },
        option("-h", "--header", help = "Header (horizontal) of desired column")
    )

    override fun run() {
        val wiki = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE)
        wiki[table]?.let {
            val ret : List<String> = when (method) {
                is Int -> Util.getCol(it, method as Int)
                is String -> Util.getCol(it, method as String, true)
                else -> {
                    echo("Missing argument: No column specifier.", err = true)
                    throw ProgramResult(1)
                }
            }

            val head = if (method is Int) Util.getHeadersHorizontalWikitable(it)[method as Int] else method as String
            if (pretty)
                echo("In column $head are:\n\t" + ret.joinToString("\n\t") { s: String -> "-$s" })
            else echo(Json.encodeToString(mapOf(head to ret)))
        } ?: run {
            echo("Invalid argument: No such cross-table found.", err = true)
            throw ProgramResult(1)
        }
    }
}