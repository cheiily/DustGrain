package dustgrain.core.fetching

import dustgrain.core.BaseMockTest
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class DataFetchServiceTest : BaseMockTest({
    val dataFetchService by lazy { DataFetchService(mockClient, mockConfig) }

    feature("DataFetchService") {
        scenario("[MOCK API] should fetch table list") {
            // given
            thereAreCargoTables()

            // when
            val list = dataFetchService.getTableList()

            // then
            list.shouldNotBeNull()
            list.shouldNotBeEmpty()
            list.shouldContain("Table1")
            list.size shouldBe 3
        }
    }
})
