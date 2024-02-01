package com.example.currencyexchanger.app.di

import com.example.currencyexchanger.homescreen.HomeScreenViewModel
import com.example.currencyexchanger.homescreen.list.HoldingsRvMapper
import com.example.currencyexchanger.homescreen.list.HoldingsRvMapperImpl
import com.example.currencyexchanger.homescreen.managers.HoldingsManager
import com.example.currencyexchanger.homescreen.network.ExchangeRatesClient
import com.example.currencyexchanger.homescreen.network.repository.ExchangeRatesRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeScreenModule {

    @Provides
    fun provideHomeScreenViewModel(
        exchangeRatesRepository: ExchangeRatesRepository,
        holdingsManager: HoldingsManager,
        holdingsRvMapper: HoldingsRvMapper
    ) =
        HomeScreenViewModel(exchangeRatesRepository, holdingsManager, holdingsRvMapper)

    @Provides
    fun provideCurrencyExchangeClient(retrofit: Retrofit): ExchangeRatesClient =
        retrofit.create(ExchangeRatesClient::class.java)

    @Provides
    fun provideHoldingsRvMapper(): HoldingsRvMapper = HoldingsRvMapperImpl()
}
