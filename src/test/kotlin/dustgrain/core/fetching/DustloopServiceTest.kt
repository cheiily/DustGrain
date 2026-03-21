package dustgrain.core.fetching

import dustgrain.core.BaseMockTest
import dustgrain.core.DustloopErrorException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DustloopServiceTest : BaseMockTest({
    val dustloopService by lazy { DustloopService(mockClient) }

    feature("DustloopService") {
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

        scenario("[MOCK API] should handle MediaWiki error response (errorclass)") {
            // given
            thereIsAMediaWikiError("errorclass")

            // then
            shouldThrow<DustloopErrorException> {
                dustloopService.getTableList()
            }.let { logger.error { it } }
        }

        scenario("[MOCK API] should handle MediaWiki error response (asterisk)") {
            // given
            thereIsAMediaWikiError("asterisk")

            // then
            shouldThrow<DustloopErrorException> {
                dustloopService.getTableList()
            }.let { logger.error { it } }
        }
    }
})
