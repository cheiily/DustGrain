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

class Cell : CommonArgs("Extract a cell (crossing) from a cross-table") {
    val table by argument(help = "Table to describe.")
    val row by mutuallyExclusiveOptions(
        option("-ri", "-yi", "--rowindex", help = "Index of desired row.").convert { v -> v.toInt() },
        option("-rh", "-yh", "--rowheader", help = "Header (vertical) of desired row.")
    )
    val col by mutuallyExclusiveOptions(
        option("-ci", "-xi", "--colindex", help = "Index of desired column").convert { v -> v.toInt() },
        option("-ch", "-xh", "--colheader", help = "Header (horizontal) of desired column")
    )

    override fun aliases(): Map<String, List<String>> = mapOf(
        "cross" to listOf("cell")
    )

    override fun run() {
        var indices : Boolean = false
        var ri : Int = 0
        var ci : Int = 0
        var rh : String = ""
        var ch : String = ""

        if ( row == null ) {
            echo("Missing argument: No row specifier.", err = true)
            throw ProgramResult(1)
        }
        if ( col == null ) {
            echo("Missing argument: No column specifier.", err = true)
            throw ProgramResult(1)
        }

        if ( !( (row is Int && col is Int) || (row is String && col is String) ) ) {
            echo("Argument type mismatch: Can only use both indices or both headers to find a cell by.", err = true)
            throw ProgramResult(1)
        }

        if ( row is Int && col is Int ) {
            indices = true
            ri = row as Int
            ci = col as Int
        } else if ( row is String && col is String ) {
            rh = row as String
            ch = col as String
        }

        val wiki = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE)
        if (!wiki.containsKey(table)) {
            echo("Invalid argument: No such cross-table found.", err = true)
            throw ProgramResult(1)
        }

        if (indices) {
            val ret = Util.getCell(wiki[table]!!, ri, ci, true)
            if (pretty)
                echo("Crossing X:$ci x Y:$ri is $ret")
            else echo(Json.encodeToString(mapOf("${ri}x${ci}" to ret)))
        } else {
            val ret = Util.getCell(wiki[table]!!, rh, ch, true)
            if (pretty)
                echo("Crossing $rh x $ch is $ret")
            else echo(Json.encodeToString(mapOf("${rh}x${ch}" to ret)))
        }
    }
}