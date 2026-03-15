package dustgrain.cli.commands.list

import dustgrain.cli.commands.CommonArgs
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import dustgrain.model.Model
import kotlin.collections.List

class Tables : CommonArgs("List tables available for this character.") {
    val onlyCargo by option("-c", "--cargo").flag()
    val onlyData by option("-d", "--data").flag()
    val onlyWiki by option("-w", "--wiki").flag()

    override fun run() {
        Model.timeout = timeout

        val ret : MutableMap<String, List<String>> = mutableMapOf()

        var car = onlyCargo;
        var dat = onlyData;
        var wik = onlyWiki;
        if ( !car && !dat && !wik ) {
            car = true; dat = true; wik = true;
        }

        if (car) {
            ret["Cargo Tables"] = Model.scrapeTables(wiki, character, Model.TableType.CARGO_TABLE).keys.toList()
        }
        if (dat) {
            ret["Data Tables"] = Model.scrapeTables(wiki, character, Model.TableType.DATA_TABLE).keys.toList()
        }
        if (wik) {
            ret["Cross Tables"] = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE).keys.toList()
        }

        if (pretty)
            echo(ret.entries.joinToString("\n") {
                entry -> "${entry.key}:\n\t" + entry.value.joinToString("\n\t") { s: String -> "- $s" }
            })
        else echo(Json.encodeToString(ret))
    }

}