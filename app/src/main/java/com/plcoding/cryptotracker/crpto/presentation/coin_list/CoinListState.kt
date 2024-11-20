package com.plcoding.cryptotracker.crpto.presentation.coin_list

import androidx.compose.runtime.Immutable
import com.plcoding.cryptotracker.crpto.presentation.model.CoinUi

@Immutable
data class CoinListState(
    val isLoading: Boolean = false,
    val coin: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)