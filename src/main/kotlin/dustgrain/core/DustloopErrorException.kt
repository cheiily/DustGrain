package dustgrain.core

import dustgrain.core.fetching.DustloopErrorResponse
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class DustloopErrorException(
    httpResponse: HttpResponse,
    error: DustloopErrorResponse,
) : ResponseException(
    httpResponse,
    "Dustloop API Error: $error"
)