package dustgrain.core.formatting

import dustgrain.core.domain.DataField
import dustgrain.core.domain.DataGrain
import dustgrain.core.domain.DataHeader
import dustgrain.core.fetching.DataFetchService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

class FormattingService(
    val dataFetchService: DataFetchService
) {
    val logger = KotlinLogging.logger{}


    fun format(data: List<Pair<DataField, DataHeader>>): List<DataGrain> =
        data.map {
            matchFormat(it.first, it.second)
                .format(it)
        }


    // ========== Formatter Implementations ==========
    val formatPass = Formatter { (data, head) ->
        DataGrain(
            name = data.name,
            contents = head.parseList(data.content)
        )
    }

    val formatErrorPass = Formatter { (data, head) ->
        logger.warn { "Unknown cargo type: ${data.type}" }
        DataGrain(
            name = data.name,
            contents = listOf(data.content)
        )
    }

    val formatImage = Formatter { (data, head) ->
        val filenames = head.parseList(data.content)
        val fileUrls = runBlocking {
                filenames.map { async {
                    dataFetchService.getImageUrl(it)
                }}.awaitAll()
            }

        DataGrain(
            name = data.name,
            contents = fileUrls
        )
    }

    val formatWikitext = Formatter { (data, head) ->
        TODO("See issue #14")
    }


    // ========== Misc ==========
    fun matchFormat(data: DataField, header: DataHeader): Formatter =
        when (header.type) {
            FormatterRef.IMAGE -> formatImage
            FormatterRef.WIKITEXT -> formatWikitext
            FormatterRef.PASS -> formatPass
            FormatterRef.PASS_ERROR -> formatErrorPass
        }


    private fun DataHeader.parseList(content: String): List<String> =
        if (delimiter == null)
            listOf(content)
        else
            content.split(delimiter)

}

