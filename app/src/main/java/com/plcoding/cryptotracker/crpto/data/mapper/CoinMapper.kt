package com.plcoding.cryptotracker.crpto.data.mapper

import com.plcoding.cryptotracker.crpto.data.networking.dto.CoinDto
import com.plcoding.cryptotracker.crpto.domain.Coin

fun CoinDto.toCoin() : Coin{
    return Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        changedPercent24Hr = changePercent24Hr
    )
}