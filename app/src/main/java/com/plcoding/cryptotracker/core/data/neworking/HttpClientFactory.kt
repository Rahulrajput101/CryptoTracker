package com.plcoding.cryptotracker.core.data.neworking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    // Function to create and configure an HttpClient instance
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {

            // Install Logging feature for debugging HTTP requests and responses
            install(Logging) {
                level = LogLevel.ALL // Log everything: headers, body, etc.
                logger = Logger.ANDROID // Use Android's log system to display logs
            }

            // Install ContentNegotiation for handling content types like JSON
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true // Ignore fields in JSON response that are not in the data class
                    }
                )
            }

            // Set default configurations for all requests made with this client
            defaultRequest {
                contentType(ContentType.Application.Json) // Add "Content-Type: application/json" header by default
            }
        }
    }
}
