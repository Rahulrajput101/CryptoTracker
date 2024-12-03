package com.plcoding.cryptotracker.crpto.presentation.model

import androidx.annotation.DrawableRes
import com.plcoding.cryptotracker.crpto.domain.Coin
import com.plcoding.cryptotracker.core.presentation.util.getDrawableIdForCoin
import com.plcoding.cryptotracker.crpto.domain.CoinPrice
import com.plcoding.cryptotracker.crpto.presentation.coin_detail.DataPoint
import java.text.NumberFormat

data class CoinUi(
    val id : String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd : DisplayableNumber,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes : Int,
    val coinPriceHistory: List<DataPoint> = emptyList(),
)

data class DisplayableNumber(
    val value : Double,
    val formatted : String
)

fun Coin.toCoinUi() : CoinUi {
    return CoinUi(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd.toDisplayableNumber(),
        priceUsd = priceUsd.toDisplayableNumber(),
        changePercent24Hr = changedPercent24Hr.toDisplayableNumber(),
       iconRes =   getDrawableIdForCoin(symbol)
    )
}

fun Double.toDisplayableNumber() : DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance(java.util.Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return DisplayableNumber(
        value = this,
        formatted =formatter.format(this)
    )
}