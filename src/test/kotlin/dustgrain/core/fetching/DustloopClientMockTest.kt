package dustgrain.core.fetching

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import dustgrain.core.BaseMockTest
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DustloopClientMockTest : BaseMockTest({
    val dustloopClient by lazy { DustloopClient(mockClient) }

    feature("DustloopClient#getTableList") {
        scenario("[MOCK API] should fetch table list") {
            // given
            thereAreCargoTables()

            // when
            val list = dustloopClient.getTableList().cargotables

            // then
            list.shouldNotBeNull()
            list.shouldNotBeEmpty()
            list.shouldContain("Table1")
            list.size shouldBe 3
        }
    }

    feature("DustloopClient#getTableHeaders") {
        scenario("[MOCK API] should fetch header list") {
            // given
            val tableName = "mocktable1"
            thereAreCargoFields(tableName)

            // when
            val headers = dustloopClient.getTableHeaders(tableName)

            // then
            headers.shouldNotBeNull()
            headers.cargofields.shouldNotBeEmpty()
            headers.cargofields.shouldContainKey(TableHeaderResponse.CargoField("chara"))
            headers.cargofields[TableHeaderResponse.CargoField("chara")]!!.shouldNotBeNull()
            headers.cargofields[TableHeaderResponse.CargoField("notes")]!! shouldBe TableHeaderResponse.CargoFieldData(
                type = "Wikitext",
                size = null,
                isList = "",
                delimiter = "\\"
            )
            logger.info { headers }
        }
    }

    feature("DustloopClient#getTableData") {
        scenario("[MOCK API] should fetch table data (gbvsr)") {
            // given
            val request = TableDataRequest(
                tables = listOf("doesn't select here"),
                fields = listOf("_", "irrelevant")
            )
            thereIsACargoQueryResult("gbvsr")

            // when
            val response = dustloopClient.getTableData(request)

            // then
            response.shouldNotBeNull()
            response.cargoquery.shouldNotBeEmpty()
            response.cargoquery[0].data shouldContainAll mapOf(
                "chara" to "Djeeta",
                "input" to "j.LU"
            )
            logger.info { response }
        }

        scenario("[MOCK API] should fetch table data (bbcf)") {
            // given
            val request = TableDataRequest(
                tables = listOf("doesn't select here"),
                fields = listOf("_", "irrelevant")
            )
            thereIsACargoQueryResult("bbcf")

            // when
            val response = dustloopClient.getTableData(request)

            // then
            response.shouldNotBeNull()
            response.cargoquery.shouldNotBeEmpty()
            response.cargoquery[0].data shouldContainAll mapOf(
                "chara" to "Noel Vermillion",
                "damage" to "1200"
            )
            logger.info { response }
        }

        scenario("serializes cargoquery request to params") {
            // given
            val request = TableDataRequest(
                tables = listOf("Table1", "Table2"),
                fields = listOf("field1", "field2"),
                where = "field1 > 10",
                joinOn = "Table1.fieldA = Table2.fieldB",
                groupBy = "field1",
                having = "COUNT(field2) > 5",
                orderBy = "field1 DESC",
                limit = 100,
                offset = 50
            )
            thereIsACargoQueryResult("gbvsr")

            // when
            val response = dustloopClient.getTableData(request)

            // then
            response.shouldNotBeNull()
            wiremockServer.verify(
                getRequestedFor(urlPathMatching("/.*"))
                    .withQueryParam("action", equalTo("cargoquery"))
                    .withQueryParam("tables", equalTo("Table1,Table2"))
                    .withQueryParam("fields", equalTo("field1,field2"))
                    .withQueryParam("where", equalTo("field1 > 10"))
                    .withQueryParam("join_on", equalTo("Table1.fieldA = Table2.fieldB"))
                    .withQueryParam("group_by", equalTo("field1"))
                    .withQueryParam("having", equalTo("COUNT(field2) > 5"))
                    .withQueryParam("order_by", equalTo("field1 DESC"))
                    .withQueryParam("limit", equalTo("100"))
                    .withQueryParam("offset", equalTo("50"))
            )
        }
    }

    feature("DustloopClient#getImageData") {
        scenario("[MOCK API] should fetch image data") {
            // given
            thereIsImageData("1")

            // when
            val response = dustloopClient.getImageData("BBCF_Noel_Vermillion_d623D.png")

            // then
            response.shouldNotBeNull()
            response.query.pages.shouldNotBeEmpty()
            response.query.pages[0].imageinfo.shouldNotBeEmpty()
            response.query.pages[0].imageinfo[0].url shouldBe "https://www.dustloop.com/wiki/images/e/e8/BBCF_Noel_Vermillion_d623D.png"

            logger.info { response }
        }

        scenario("serializes image data request to params") {
            // given
            thereIsImageData("1")

            // when
            val response = dustloopClient.getImageData("BBCF_Noel_Vermillion_d623D.png")

            // then
            response.shouldNotBeNull()
            wiremockServer.verify(
                getRequestedFor(urlPathMatching("/.*"))
                    .withQueryParam("action", equalTo("query"))
                    .withQueryParam("prop", equalTo("imageinfo"))
                    .withQueryParam("iiprop", equalTo("url|size|mime"))
                    .withQueryParam("titles", equalTo("File:BBCF_Noel_Vermillion_d623D.png"))
                    .withQueryParam("formatversion", equalTo("2"))
            )

            // and
            val response2 = dustloopClient.getImageData("File:GBVSR_Djeeta_6L.png")

            // then
            response2.shouldNotBeNull()
            wiremockServer.verify(
                getRequestedFor(urlPathMatching("/.*"))
                    .withQueryParam("action", equalTo("query"))
                    .withQueryParam("prop", equalTo("imageinfo"))
                    .withQueryParam("iiprop", equalTo("url|size|mime"))
                    .withQueryParam("titles", equalTo("File:GBVSR_Djeeta_6L.png"))
                    .withQueryParam("formatversion", equalTo("2"))
            )
        }
    }
})
