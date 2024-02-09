package model

import org.jsoup.nodes.Element

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
}