package com.plcoding.cryptotracker.crpto.data.mapper

import com.plcoding.cryptotracker.crpto.data.networking.dto.CoinDto
import com.plcoding.cryptotracker.crpto.data.networking.dto.CoinPriceDto
import com.plcoding.cryptotracker.crpto.domain.Coin
import com.plcoding.cryptotracker.crpto.domain.CoinPrice
import java.time.Instant
import java.time.ZoneId

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

fun CoinPriceDto.toCoinPrice(): CoinPrice{
    return  CoinPrice(
        priceUsd = priceUsd,
        dateTime = Instant
            .ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
    )
}