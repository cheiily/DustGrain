package dustgrain.core.cache

import dustgrain.core.domain.DataHeader
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

fun interface Encoder<T, R> {
    fun encode(value: T): R
}

fun interface Decoder<T, R> {
    fun decode(value: R): T
}

abstract class Codec<T, R> (
    val encoder: Encoder<T, R>,
    val decoder: Decoder<T, R>
) {
    fun encode(value: T): R = encoder.encode(value)
    fun decode(value: R): T = decoder.decode(value)
}

class StringCodec : Codec<String, String>(
    encoder = Encoder { it },
    decoder = Decoder { it }
)

class DataHeaderListCodec : Codec<List<DataHeader>, String>(
    encoder = Encoder { list -> json.encodeToString(listSerializer, list) },
    decoder = Decoder { string -> json.decodeFromString(listSerializer, string) }
) {
    companion object {
        private val json = Json
        private val listSerializer = ListSerializer(DataHeader.serializer())
    }
}
