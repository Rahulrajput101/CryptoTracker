package com.plcoding.cryptotracker.crpto.domain

data class Coin(
    val id : String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changedPercent24Hr : Double
)
