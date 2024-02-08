package model

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object Model {
    private fun scrapeControls(wiki: String, char: String): kotlin.collections.List<Element> {
        val url = "https://www.dustloop.com/w/$wiki/$char/Frame_Data"
        val doc = Jsoup.connect(url)
            .timeout(5000)
            .get()

        return doc.getElementsByClass("details-control").filter { elem -> elem.tagName() == "td" }
    }

    fun getData(wiki: String, char: String, move: String) : Map<String, String> {
        val cntrl = scrapeControls(wiki, char)
        val data = try {
            cntrl.first { elem -> elem.nextElementSibling()?.text() == move }.siblingElements()
        } catch (_: NoSuchElementException) {
            error("No such move found, try `dust list <wiki> <char>`.")
        }
        //drop the sort control (it's also invisible anyway)
        val headers = Util.getParentTable(data[0])?.select("thead")?.first()?.select("th")?.drop(1)

        val htext = headers?.map(Element::text)
        val dtext = data?.map(Element::text)

        if (htext == null || dtext == null)
            error("Parsing data failed")

        return htext.zip(dtext).toMap()
    }

    fun listMoves(wiki: String, char: String): List<String> {
        val cntrl = scrapeControls(wiki, char)
        return cntrl.mapNotNull { elem -> elem.nextElementSibling()?.text() }
    }
}