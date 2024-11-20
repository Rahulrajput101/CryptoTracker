package com.plcoding.cryptotracker.di

import com.plcoding.cryptotracker.core.data.neworking.HttpClientFactory
import com.plcoding.cryptotracker.crpto.data.networking.RemoteCoinDataSource
import com.plcoding.cryptotracker.crpto.domain.CoinDataSource
import com.plcoding.cryptotracker.crpto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

    viewModelOf(::CoinListViewModel)
}