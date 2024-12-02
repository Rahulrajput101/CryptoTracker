package com.plcoding.cryptotracker.crpto.domain

import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.core.domain.util.NetworkError
import java.time.ZonedDateTime

interface CoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
    suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError>
}