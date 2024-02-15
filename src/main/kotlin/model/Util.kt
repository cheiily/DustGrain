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

    //todo for wikitables vvv
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

    fun getCol(table: Element, header: String) : List<String> {
        val headers = getHeaders(table)
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
    //todo for wikitables ^^^


    fun getRow(table: Element, firstCell: String, wikitable: Boolean = false) : List<String> {
        val selectcell = if (wikitable) "td, th" else "td"

        return table.select("tbody").first()
            ?.select("tr")?.first { element ->
                element.select(selectcell).first()?.text() == firstCell
            }?.select(selectcell)
            ?.mapNotNull {
                element -> if (wikitable) element.ownText() else element.text()
            }.orEmpty()
    }

    fun getRow(table: Element, index: Int, wikitable: Boolean = false) : List<String> {
        return if (wikitable) {
            table.select("tbody").first()
                ?.select("tr")?.get(index + 1)
                ?.select("td, th")
                ?.mapNotNull {
                    element -> element.ownText()
                }.orEmpty()
        } else {
            table.select("tbody").first()
                ?.select("tr")?.get(index)
                ?.select("td")
                ?.mapNotNull {
                    element -> element.text()
                }.orEmpty()
        }
    }

}