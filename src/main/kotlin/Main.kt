import cli.DustGrain
import cli.commands.data.Data
import cli.commands.data.Move
import cli.commands.data.System
import cli.commands.find.Find
import cli.commands.list.*
import cli.commands.list.List
import cli.commands.wikitable.*
import com.github.ajalt.clikt.core.subcommands
import model.Model
import model.Util
import model.Util.listMoves
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import cli.commands.find.Cell as FindCell
import cli.commands.find.Moves as FindMoves
import cli.commands.wikitable.Cell as WikitableCell

/**
 * Delegates execution to the main command for CLI handling.
 * @see DustGrain
 * @see List
 * @see Data
 */
fun main(args: Array<String>) =
    DustGrain().subcommands(
        List().subcommands(Moves(), Stat(), Stats(), Tables()),
        Data().subcommands(Move(), System()),
        Wikitable().subcommands(Headers(), Row(), Column(), WikitableCell()),
        Find().subcommands(FindCell(), FindMoves())
    ).main(args)

//fun main(args: Array<String>) {
//    val url = "https://www.dustloop.com/w/GBVSR/Djeeta/Frame_Data"
//    val doc = Jsoup.connect(url)
//        .timeout(5000)
//        .get()
//
//    val movemap: Map<String, Element>
//
//    //unify tables
////    doc.getElementsByClass("details-control").forEach( Element::remove )
////
////    doc.getElementsByClass("section-heading")
////        .let {
////            return@let it.subList(1, it.size - 1)
////        }.also {
////            val keys = it.mapNotNull { element -> element.text() }
////            val vals = it.mapNotNull { element -> element.nextElementSibling()?.select("table")?.first() }
////
////            movemap = keys.zip(vals).toMap()
////        }
//
////    println(movemap)
//
////    println( movemap["Normal Moves"]?.select("tbody")?.first()?.select("tr")?.map { element -> element.firstElementChild()?.text() } )
////    println( movemap["System Data"]?.select("tbody")?.first()?.select("td")?.map { element -> element.text() } )
//
//    movemap = Model.scrapeTables("BBCF", "Noel_Vermillion")
//
//    println(movemap.listMoves("Normal Moves"))
//    println(movemap["Normal Moves"]?.let { Util.getHeaders(it) })
//    println(movemap["Normal Moves"]?.let { Util.getRow(it, "5A") })
//    println(movemap["Normal Moves"]?.let { Util.getCol(it, "input") })
//    println(movemap["Normal Moves"]?.let { Util.getCol(it, 1) })
//    println(movemap["Normal Moves"]?.let { Util.getCell(it, 0, 0, false) })
//    println(movemap["Normal Moves"]?.let { Util.getStat(it, "j.2C", "startup") })
//    println()
//
//    val sysmap = Model.scrapeTables("BBCF", "Noel_Vermillion", Model.TableType.CARGO_TABLE)
//    println(sysmap["System Data"]?.let { Util.getHeaders(it) })
//    println(sysmap["System Data"]?.let { Util.getRow(it, "Noel Vermillion") })
//    println(sysmap["System Data"]?.let { Util.getRow(it, 0) })
//    println()
//
//    val wikimap = Model.scrapeTables("BBCF", "Noel_Vermillion", Model.TableType.WIKI_TABLE)
//    println(wikimap["Ground Revolver Action Table"]?.let { Util.getRow(it, 2, true) })
//    println(wikimap["Air Revolver Action Table"]?.let { Util.getRow(it, "j.A", true) })
//    println(wikimap["Air Revolver Action Table"]?.let { Util.getCol(it, 0) })
//    println(wikimap["Air Revolver Action Table"]?.let { Util.getCol(it, "Cancels", true) })
//    println(wikimap["Ground Revolver Action Table"]?.let { Util.getHeadersWikitable(it) })
//    println(wikimap["Ground Revolver Action Table"]?.let { Util.getCell(it, 0, 0, true) })
//    println()
//
//    val dbfzmap = Model.scrapeTables("DBFZ", "Android_18", Model.TableType.WIKI_TABLE)
//    println(dbfzmap["Air Z Combo"]?.let { Util.getHeadersWikitable(it) })
//    println()
//
//    val p4map = Model.scrapeTables("P4U2R", "Marie", Model.TableType.WIKI_TABLE)
//    println(p4map["Ground P Combo Table"]?.let { Util.getHeadersWikitable(it) })
//    println(p4map["Ground P Combo Table"]?.let { Util.getHeadersHorizontalWikitable(it) })
//    println(p4map["Ground P Combo Table"]?.let { Util.getHeadersVerticalWikitable(it) })
//}

/// TODO: cli structure
//list
//	moves
//	stat (column)
//	stats (headers)
//	tables
//
//data
//	move-all
//	move-stat
//
//wikitable
//	headers
//	row
//	column
//	cell (cross)
//
//find
//	cell
//	moves for stat not-empty/null
//	moves for stat under/equal/above/containing