package dustgrain.core.fetching

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import dustgrain.core.BaseMockTest
import dustgrain.core.DustloopErrorException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DustloopServiceIT : BaseMockTest({
    val dustloopService by lazy { DustloopService(mockClient) }

    feature("DustloopService#getTableList") {
        scenario("[MOCK API] should fetch table list") {
            // given
            thereAreCargoTables()

            // when
            val list = dustloopService.getTableList().cargotables

            // then
            list.shouldNotBeNull()
            list.shouldNotBeEmpty()
            list.shouldContain("Table1")
            list.size shouldBe 3
        }
    }

    feature("DustloopService#getTableHeaders") {
        scenario("[MOCK API] should fetch header list") {
            // given
            val tableName = "mocktable1"
            thereAreCargoFields(tableName)

            // when
            val headers = dustloopService.getTableHeaders(tableName)

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

    feature("DustloopService#getTableData") {
        scenario("[MOCK API] should fetch table data (gbvsr)") {
            // given
            val request = TableDataRequest(
                tables = listOf("doesn't select here"),
                fields = listOf("_", "irrelevant")
            )
            thereIsCargoQueryResult("gbvsr")

            // when
            val response = dustloopService.getTableData(request)

            // then
            response.shouldNotBeNull()
            response.cargoquery.shouldNotBeEmpty()
            response.cargoquery[0].title shouldContainAll mapOf(
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
            thereIsCargoQueryResult("bbcf")

            // when
            val response = dustloopService.getTableData(request)

            // then
            response.shouldNotBeNull()
            response.cargoquery.shouldNotBeEmpty()
            response.cargoquery[0].title shouldContainAll mapOf(
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
            thereIsCargoQueryResult("gbvsr")

            // when
            val response = dustloopService.getTableData(request)

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
})
