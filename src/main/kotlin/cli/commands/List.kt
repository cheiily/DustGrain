package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model

class List : CliktCommand(help = "List moves for the specified character") {
    private val wiki by argument(help = "Dustloop sub-wiki")
    private val char by argument(help = "Character's name as seen in the url")
    private val pretty by option("-p", "--pretty").flag().help("Human-readable text")

    override fun run() {
        val moves = Model.listMoves(wiki, char)

        if (pretty) echo("Moves: $moves")
        else echo(Json.encodeToString(mapOf("moves" to moves)))
    }
}