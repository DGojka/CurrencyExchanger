package com.example.currencyexchanger.app.di

import com.example.currencyexchanger.homescreen.network.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideExchangeRatesMapper(): ExchangeRatesMapper = ExchangeRatesMapperImpl()

    @Singleton
    @Provides
    fun provideExchangeRepository(
        exchangeRatesMapper: ExchangeRatesMapper,
        client: ExchangeRatesClient
    ): ExchangeRatesRepository =
        ExchangeRatesRepositoryImpl(exchangeRatesMapper, client)
}
