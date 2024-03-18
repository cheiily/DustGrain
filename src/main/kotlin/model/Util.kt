package model

import org.jsoup.nodes.Element
import java.lang.IndexOutOfBoundsException

object Util {
    /**
     * Tries to recursively obtain a parent element of tag-type "table".
     * Will return null if none found.
     */
    fun getParentTable(tabElem: Element?) : Element? {
        if (tabElem == null) return null
        if (tabElem.tagName() == "table") return tabElem

        return getParentTable(tabElem.parent())
    }

    fun Map<String, Element>.listMoves(category: String): List<String> {
        return this[category]
            ?.select("tbody")?.first()
            ?.select("tr")
            ?.mapNotNull {
                element -> element.firstElementChild()?.text()
            }.orEmpty()
    }

    //mapnotnull & catch take care of first column in wikitables because there's no td elems
    fun getCol(table: Element, index: Int) : List<String> {
        return table.select("tbody").first()
            ?.select("tr")
            ?.mapNotNull { tr ->
                try {
                    tr.children().filter { child -> child.tagName() == "td" }[index].text()
                } catch (ex: IndexOutOfBoundsException) {
                    null
                }
            }.orEmpty()
    }

    fun getCol(table: Element, header: String, wikitable: Boolean = false) : List<String> {
        val headers = if (wikitable) getHeadersHorizontalWikitable(table) else getHeaders(table)
        var index = -1
        for (hdr in headers) {
            ++index
            if ( hdr == header ) {
                return getCol(table, index)
            }
        }
        return emptyList()
    }

    fun getHeaders(table: Element) : List<String> {
        return table.select("thead").first()
            ?.select("th")
            ?.mapNotNull {
                    element -> element.text()
            }.orEmpty()
    }

    fun getHeadersWikitable(table: Element) : Pair<List<String>, List<String>> {
        return Pair( getHeadersHorizontalWikitable(table), getHeadersVerticalWikitable(table) )
    }

    fun getHeadersHorizontalWikitable(table: Element) : List<String> {
        return table.select("tbody").first()
            ?.select("tr")?.first()
            ?.select("th")?.mapNotNull {
                header -> header.text()
            }?.drop(1) //cull the corner element -> it's visual padding
            .orEmpty()
    }

    fun getHeadersVerticalWikitable(table: Element) : List<String> {
        return table.select("tbody").first()
            ?.select("tr")?.mapNotNull {
                row -> row.select("th").first()?.let { getTooltipText(it) } //todo replace with getToolboxCellText
            }?.drop(1) //cull the corner element -> it's visual padding
            .orEmpty()
    }

    //cell -> th element
    fun getTooltipText(cell: Element) : String {
        return cell.ownText() + cell.children().joinToString("") { child ->
            child.ownText() + child.children().joinToString ("") { deepChild ->
                deepChild.ownText()
            }
        }
    }

    fun getRow(table: Element, rowHead: String) : List<String> {
        return table.select("tbody").first()
            ?.select("tr")?.first { element ->
                element.select("th, td").first()?.let { getTooltipText(it) } == rowHead
            }?.select("td")
            ?.mapNotNull {
                element -> element.text()
            }.orEmpty()
    }

    fun getRow(table: Element, index: Int, wikitable: Boolean = false) : List<String> {
        val idx = if (wikitable) index + 1 else index

        return table.select("tbody").first()
            ?.select("tr")?.get(idx)
            ?.select("td")
            ?.mapNotNull {
                    element -> element.text()
            }.orEmpty()
    }

    fun getCell(table: Element, row: Int, col: Int, wikitable: Boolean) : String {
        return getRow(table, row, wikitable)[col]
    }

    fun getCell(table: Element, rowhead: String, colhead: String, wikitable: Boolean) : String {
        var colidx = -1
        for (hdr in if (wikitable) Util.getHeadersHorizontalWikitable(table) else Util.getHeaders(table)) {
            colidx++
            if (hdr == colhead) break
        }
        return getRow(table, rowhead)[colidx]
    }

    fun getStat(table: Element, move: String, stat: String) : String {
        val col = getCol(table, stat)
        val idx = getCol(table, "input").indexOf(move)

        return col[idx]
    }

    fun rows(table: Element) : List<List<String>> {
        return table.select("tbody").first()
            ?.select("tr")
            ?.mapNotNull {
                row -> row.select("td")?.mapNotNull { element -> element.text() }
            }.orEmpty()
    }

    //todo
//    fun cols(table: Element) : List<List<String>> {
//
//    }

}