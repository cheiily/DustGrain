package model

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object Model {
    /**
     * Common function for extracting table elements from dustloop wiki.
     *
     * The utilized gimmick for pulling data from dustloop is the fact each table-row has a details-control cell,
     *  which shows or hides the image previews. This function gets all such cells from the html.
     *
     * @param wiki Sub-wiki name, as observed in the actual url.
     * @param char Character, as observed in the actual url.
     * @return List of HTML Elements, corresponding to table cells with the details-control class of "td" tag-name.
     * @see Element
     */
    private fun scrapeControls(wiki: String, char: String): kotlin.collections.List<Element> {
        val url = "https://www.dustloop.com/w/$wiki/$char/Frame_Data"
        val doc = Jsoup.connect(url)
            .timeout(5000)
            .get()

        return doc.getElementsByClass("details-control").filter { elem -> elem.tagName() == "td" }
    }

    /**
     * Extracts the data map from dustloop.
     *
     * Filters the table rows to find the one with matching "input" text. - First cell after details-control.
     * Gets headers via Util#getParentTable, which is accessed for "thead", and further "th" elements. First "th" element is dropped as it's the invisible sort control.
     * Actual data values are extracted from the details-control's sibling cells.
     * Headers are then zipped with values and returned as a map.
     *
     * @param wiki Sub-wiki name, as observed in the actual url.
     * @param char Character name, as observed in the actual url.
     * @param move Move's input, as observed under the "input" rubric in the frame data table.
     * @return Map of a move's properties keyed to the property name.
     * @see scrapeControls
     * @see Util.getParentTable
     */
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

    /**
     * Extracts the move input list from dustloop.
     *
     * Simply gets the first sibling cell of each control cell obtained from the website.
     * @param wiki Sub-wiki name, as observed in the actual url.
     * @param char Character name, as observed in the actual url.
     * @return List of input strings
     * @see scrapeControls
     */
    fun listMoves(wiki: String, char: String): List<String> {
        val cntrl = scrapeControls(wiki, char)
        return cntrl.mapNotNull { elem -> elem.nextElementSibling()?.text() }
    }
}