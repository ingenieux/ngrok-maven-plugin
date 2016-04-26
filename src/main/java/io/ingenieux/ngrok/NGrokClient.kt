package io.ingenieux.ngrok

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost

object NGrokClient {
    val baseUrl: String = "http://127.0.0.1:4040/api/"

    val objectMapper = jacksonObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)

    fun get(path: String): JsonNode {
        return responseParser((baseUrl + path).httpGet())
    }

    fun post(path: String, payload: Any): JsonNode {
        val payloadAsString = objectMapper.writeValueAsString(payload)

        System.err.println("payloadAsString: ${payloadAsString}")

        val payloadAsByteArray = payloadAsString.toByteArray()

        return responseParser(
                (baseUrl + path)
                        .httpPost()
                        .body(payloadAsByteArray)
                        .header("Content-Type" to "application/json")
        )
    }

    private fun responseParser(s: Request): JsonNode {
        //System.err.println("curl-able: ${s.cUrlString()}")

        val (request, response, result) = s.responseString()

        val resultAsString = result.get()

        when (response.httpStatusCode) {
            in 200..201 -> return objectMapper.readTree(resultAsString)
            else -> throw IllegalStateException("Non-200 Code ${response.httpStatusCode} for ${request.url}: ${resultAsString}")
        }
    }

}