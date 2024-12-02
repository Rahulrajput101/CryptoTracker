package com.plcoding.cryptotracker.crpto.data.networking

import com.plcoding.cryptotracker.core.data.neworking.constructUrl
import com.plcoding.cryptotracker.core.data.neworking.safeCall
import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.core.domain.map
import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.crpto.data.mapper.toCoin
import com.plcoding.cryptotracker.crpto.data.mapper.toCoinPrice
import com.plcoding.cryptotracker.crpto.data.networking.dto.CoinHistoryDto
import com.plcoding.cryptotracker.crpto.data.networking.dto.CoinResponseDto
import com.plcoding.cryptotracker.crpto.domain.Coin
import com.plcoding.cryptotracker.crpto.domain.CoinDataSource
import com.plcoding.cryptotracker.crpto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource (
    private val httpClient: HttpClient
): CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinResponseDto> {
           val `http-response` =  httpClient.get(
                urlString = constructUrl("/assets")
            )
            `http-response`
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

        val endMillis = end
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
        return safeCall<CoinHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ){
                parameter("interval","h6")
                parameter("start",startMillis)
                parameter("end",endMillis)
            }
        }.map {response ->
            response.data.map { it .toCoinPrice()}

        }

    }
}