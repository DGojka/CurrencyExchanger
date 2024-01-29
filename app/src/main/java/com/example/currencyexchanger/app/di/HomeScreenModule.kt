package com.example.currencyexchanger.app.di

import com.example.currencyexchanger.homescreen.HomeScreenViewModel
import com.example.currencyexchanger.homescreen.network.ExchangeRatesClient
import com.example.currencyexchanger.homescreen.network.ExchangeRatesRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeScreenModule {

    @Provides
    fun provideHomeScreenViewModel(exchangeRatesRepository: ExchangeRatesRepository) =
        HomeScreenViewModel(exchangeRatesRepository)

    @Provides
    fun provideCurrencyExchangeClient(retrofit: Retrofit): ExchangeRatesClient =
        retrofit.create(ExchangeRatesClient::class.java)
}
