package model

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object Util {
    fun getParentTable(tabElem: Element?) : Element? {
        if (tabElem == null) return null
        if (tabElem.tagName() == "table") return tabElem

        return getParentTable(tabElem.parent())
    }


}