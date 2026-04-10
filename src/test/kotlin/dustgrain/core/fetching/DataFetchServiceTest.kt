package dustgrain.core.fetching

import dustgrain.core.ApiMockTest
import dustgrain.core.formatting.FormatterRef
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class DataFetchServiceTest : ApiMockTest({
    feature("DataFetchService#getTableHeaders") {
        scenario("parses data headers from client response") {
            // given
            thereAreCargoFields("mocktable1")

            // when
            val tableName = "mocktable1"
            val fetchResult = mockDataFetchService.getTableHeaders(tableName)

            // then
            val chara = fetchResult.first { it.name == "chara" }.shouldNotBeNull()
            val input = fetchResult.first { it.name == "input" }.shouldNotBeNull()
            val images = fetchResult.first { it.name == "images" }.shouldNotBeNull()
            val notes = fetchResult.first { it.name == "notes" }.shouldNotBeNull()
            val error = fetchResult.first { it.name == "error" }.shouldNotBeNull()

            chara.type shouldBeEqual FormatterRef.PASS
            chara.delimiter.shouldBeNull()

            input.type shouldBeEqual FormatterRef.WIKITEXT
            input.delimiter.shouldBeNull()

            images.type shouldBeEqual FormatterRef.IMAGE
            images.delimiter!! shouldBeEqual "\\"

            notes.type shouldBeEqual FormatterRef.WIKITEXT
            notes.delimiter!! shouldBeEqual "\\"

            error.type shouldBeEqual FormatterRef.PASS_ERROR
        }
    }



    feature("DataFetchService#getTableList") {
        scenario("should extract result from client response") {
            // given
            thereAreCargoTables()

            // when
            val response = mockDustloopClient.getTableList()
            val fetchResult = mockDataFetchService.getTableList()

            // then
            fetchResult shouldBeEqual response.cargotables
            fetchResult.shouldNotBeEmpty()
        }
    }

    feature("DataFetchService#getTableData") {
        scenario("should extract result from client response") {
            // given
            val request = TableDataRequest(
                tables = listOf("doesn't select here"),
                fields = listOf("_", "irrelevant")
            )
            thereIsACargoQueryResult("bbcf")

            // when
            val response = mockDustloopClient.getTableData(request)
            val fetchResult = mockDataFetchService.getTableData(request)

            // then
            fetchResult shouldBeEqual response.cargoquery.map { it.data }
            fetchResult.shouldNotBeEmpty()
        }
    }

    feature("DataFetchService#getImageInfo") {
        scenario("should extract result from client response") {
            // given
            val imageName = "Example.jpg"
            thereIsImageData("1")

            // when
            val response = mockDustloopClient.getImageData(imageName)
            val fetchResult = mockDataFetchService.getImageInfo(imageName)

            // then
            fetchResult shouldBeEqual response.query.pages.first().imageinfo.first()
        }
    }

})
