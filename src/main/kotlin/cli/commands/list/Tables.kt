package cli.commands.list

import cli.commands.CommonArgs
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Model
import kotlin.collections.List

class Tables : CommonArgs("List tables available for this character.") {
    val onlyData by option("-d", "--data").flag()
    val onlyWiki by option("-w", "--wiki").flag()

    override fun run() {
        val ret : MutableMap<String, List<String>> = mutableMapOf()

        if (!onlyWiki) {
            ret["Data Tables"] = Model.scrapeTables(wiki, character, Model.TableType.DATA_TABLE).keys.toList()
        }
        if (!onlyData) {
            ret["Cross Tables"] = Model.scrapeTables(wiki, character, Model.TableType.WIKI_TABLE).keys.toList()
        }

        echo(Json.encodeToString(ret))
    }

}