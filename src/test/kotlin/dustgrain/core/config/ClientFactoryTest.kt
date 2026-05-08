package dustgrain.core.config

import dustgrain.core.Application
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.nulls.shouldNotBeNull

class ClientFactoryTest : FeatureSpec({
    fun resetApplicationForTest() {
        Application::class.java.getDeclaredField("profile").apply {
            isAccessible = true
            set(Application, null)
        }
        Application::class.java.getDeclaredField("config").apply {
            isAccessible = true
            set(Application, null)
        }
    }

    feature("Client Factory") {
        scenario("should fail when application is not initialized") {
            resetApplicationForTest()

            shouldThrow<UninitializedPropertyAccessException> {
                getHttpClient()
            }
        }

        scenario("should create a client with the correct configuration") {
            Application.initialize(AppProfile.CLI)

            val client = getHttpClient()
            client shouldNotBeNull {}
        }
    }
})
