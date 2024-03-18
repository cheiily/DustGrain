package cli.commands.list

import cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import model.Model
import model.Util

class Stats : CommonArgs("List all stats available for this table (headers).") {
    val table by argument(help = "Table to describe.")

    override fun run() {
        val datatable = Model.scrapeTables(wiki, character)
        datatable[table]?.let {
            echo(Util.getHeaders(it))
        } ?: run {
            echo("Invalid argument: No such table found.", err = true)
            throw ProgramResult(1)
        }

    }

}