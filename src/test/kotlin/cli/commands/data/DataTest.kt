package cli.commands.data

import com.github.ajalt.clikt.testing.test
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class DataTest {
    val wiki = "GBVS";
    val character = "Djeeta";
    val move = "5U"
    val stat = "active"
    val cmd = Move();

    @Test
    fun successMoveNoop() {
        val result = cmd.test("$wiki $character $move")
        assertEquals(0, result.statusCode)
        assertNotEquals("", result.stdout)
        val json = Json.parseToJsonElement(result.stdout) as JsonObject
        assertNotNull(json["$move"])
        val data = json["$move"]!! as JsonObject
        assertEquals("5U", data["input"].toString().trim('"'))
        assertEquals("-2", data["onBlock"].toString().trim('"'))
        assertEquals("All", data["guard"].toString().trim('"'))
    }

    @Test
    fun sucessMoveStat() {
        val result = cmd.test("$wiki $character $move --stat $stat")
        assertEquals(0, result.statusCode)
        assertNotEquals("", result.stdout)
        val json = Json.parseToJsonElement(result.stdout) as JsonObject
        assertEquals("5", json["active"].toString().trim('"'))
    }
}