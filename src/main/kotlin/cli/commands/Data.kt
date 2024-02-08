package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model

class Data : CliktCommand(help = "Get a move's data", name = "data") {
    private val wiki by argument(help = "Dustloop sub-wiki")
    private val char by argument(help = "Character's name as seen in the url")
    private val move by argument(help = "Character's move by input")
    private val pretty by option("-p", "--pretty").flag().help("Human-readable text")

    override fun run() {
        val data = Model.getData(wiki, char, move)

        if (pretty) echo(
            data.toList().joinToString("\n") { pair ->
                "${pair.first} = ${pair.second}"
            }
        )
        else echo(Json.encodeToString(data))
    }
}