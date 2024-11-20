package com.plcoding.cryptotracker.crpto.domain

import com.plcoding.cryptotracker.core.domain.Result
import com.plcoding.cryptotracker.core.domain.util.NetworkError

interface CoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
}