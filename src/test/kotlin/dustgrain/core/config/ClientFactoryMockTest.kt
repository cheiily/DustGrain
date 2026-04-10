package dustgrain.core.config

import dustgrain.core.ApiMockTest
import dustgrain.core.DustloopErrorException
import io.kotest.assertions.throwables.shouldThrow
import io.ktor.client.request.get

class ClientFactoryMockTest : ApiMockTest({
    feature("Configured Http Client") {

        scenario("[MOCK API] should handle MediaWiki error response (errorclass)") {
            // given
            thereIsAMediaWikiError("errorclass")

            // then
            shouldThrow<DustloopErrorException> {
                mockClient.get {}
            }.let { logger.error { it } }
        }

        scenario("[MOCK API] should handle MediaWiki error response (asterisk)") {
            // given
            thereIsAMediaWikiError("asterisk")

            // then
            shouldThrow<DustloopErrorException> {
                mockClient.get {}
            }.let { logger.error { it } }
        }
    }
})