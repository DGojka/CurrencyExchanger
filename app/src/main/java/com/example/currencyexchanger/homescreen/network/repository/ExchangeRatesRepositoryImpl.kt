package com.example.currencyexchanger.homescreen.network.repository

import android.accounts.NetworkErrorException
import com.example.currencyexchanger.extensions.inFlow
import com.example.currencyexchanger.homescreen.network.ExchangeRatesClient
import com.example.currencyexchanger.homescreen.network.data.ExchangeRates
import com.example.currencyexchanger.homescreen.network.mappers.ExchangeRatesMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ExchangeRatesRepositoryImpl(
    private val exchangeRatesMapper: ExchangeRatesMapper,
    private val client: ExchangeRatesClient
) :
    ExchangeRatesRepository {
    override suspend fun getExchangeRates(): ExchangeRates {
        return inFlow { client.getExchangeRates() }.map {
            it.body() ?: throw NetworkErrorException()
        }.map { exchangeRatesMapper.map(it) }.first()
    }
}
