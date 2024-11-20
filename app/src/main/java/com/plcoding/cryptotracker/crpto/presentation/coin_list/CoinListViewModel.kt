package com.plcoding.cryptotracker.crpto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.onError
import com.plcoding.cryptotracker.core.domain.onSuccess
import com.plcoding.cryptotracker.crpto.domain.CoinDataSource
import com.plcoding.cryptotracker.crpto.presentation.model.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state.asStateFlow()
        .onStart { loadCoins() }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinListState()
        )

    private val _events = Channel<CoinListEvent>()
    val event = _events.receiveAsFlow()

    fun onAction(action: CoinListAction){
        when(action){
            is CoinListAction.OnCoinCLick -> {

            }
            CoinListAction.OnRefresh -> {
                loadCoins()
            }
        }
    }

    private fun loadCoins(){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }

            coinDataSource
                .getCoins()
                .onSuccess { coins->
                    _state.update { it.copy(
                        isLoading = false,
                        coin = coins.map { it.toCoinUi() }
                    ) }

                }
                .onError {error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }
}