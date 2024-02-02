package com.example.currencyexchanger.app.di

import android.content.Context
import com.example.currencyexchanger.homescreen.managers.HoldingsManager
import com.example.currencyexchanger.homescreen.network.ExchangeRatesClient
import com.example.currencyexchanger.homescreen.network.mappers.ExchangeRatesMapper
import com.example.currencyexchanger.homescreen.network.mappers.ExchangeRatesMapperImpl
import com.example.currencyexchanger.homescreen.network.repository.ExchangeRatesRepository
import com.example.currencyexchanger.homescreen.network.repository.ExchangeRatesRepositoryImpl
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

    @Singleton
    @Provides
    fun provideHoldingsManager(context: Context): HoldingsManager {
        return HoldingsManager(context)
    }

}
