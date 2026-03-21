package dustgrain.core.fetching

import dustgrain.core.BaseMockTest

class DataFetchServiceTest : BaseMockTest({
    val dataFetchService by lazy { DataFetchService(mockClient, mockConfig) }

    feature("DataFetchService") {

    }
})
