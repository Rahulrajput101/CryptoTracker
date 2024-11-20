package com.plcoding.cryptotracker.crpto.data.networking

import com.plcoding.cryptotracker.core.data.neworking.constructUrl
import com.plcoding.cryptotracker.core.data.neworking.safeCall
import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.core.domain.map
import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.crpto.data.mapper.toCoin
import com.plcoding.cryptotracker.crpto.data.networking.dto.CoinResponseDto
import com.plcoding.cryptotracker.crpto.domain.Coin
import com.plcoding.cryptotracker.crpto.domain.CoinDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

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
}