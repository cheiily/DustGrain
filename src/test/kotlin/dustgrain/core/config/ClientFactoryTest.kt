package dustgrain.core.config

import dustgrain.core.Application
import dustgrain.core.cache.CacheMode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.nulls.shouldNotBeNull

class ClientFactoryTest : FeatureSpec({
    feature("Client Factory") {
        scenario("should fail when application is not initialized") {
            shouldThrow<UninitializedPropertyAccessException> {
                getHttpClient()
            }
        }

        scenario("should create a client with the correct configuration") {
            Application.initialize(AppProfile.CLI, "", CacheMode.NOOP)

            val client = getHttpClient()
            client shouldNotBeNull {}
        }
    }
})