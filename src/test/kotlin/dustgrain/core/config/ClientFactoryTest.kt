package dustgrain.core.config

import dustgrain.core.Application
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.client.plugins.defaultTransformers
import io.ktor.client.request.prepareGet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class ClientFactoryTest : FeatureSpec({
    feature("Client Factory") {
        scenario("should fail when application is not initialized") {
            shouldThrow<UninitializedPropertyAccessException> {
                getClient()
            }
        }

        scenario("should create a client with the correct configuration") {
            Application.initialize(AppProfile.CLI)

            val client = getClient()
            client shouldNotBeNull {}
        }
    }
})