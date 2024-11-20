package com.plcoding.cryptotracker.crpto.presentation.coin_list

import com.plcoding.cryptotracker.crpto.presentation.model.CoinUi

sealed interface CoinListAction {
    data class OnCoinCLick(val coinUi: CoinUi): CoinListAction
    data object OnRefresh : CoinListAction
}