package dustgrain.cli.commands.find

import dustgrain.cli.commands.CommonArgs
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dustgrain.model.Model
import dustgrain.model.Util

class Cell : CommonArgs("Find cell of data table via indices.") {
    val table by argument(help = "Table to peek.")
    val row by option("-ri", "-y", "--rowindex", help = "Index of desired row.").int().required()
    val col by option("-ci", "-x", "--colindex", help = "Index of desired column").int().required()

    override fun run() {
        Model.timeout = timeout

        val data = Model.scrapeTables(wiki, character)
        data[table]?.let {
            val v = Util.getCell(it, row, col, false)

            if (pretty) echo("${Util.getRow(it, row)[0]} ${Util.getHeaders(it)[col]} is $v")
            else echo(Json.encodeToString(mapOf("${row}x${col}" to v)))
        } ?: run {
            echo("Invalid argument: No such data table found.", err = true)
            throw ProgramResult(1)
        }

    }
}