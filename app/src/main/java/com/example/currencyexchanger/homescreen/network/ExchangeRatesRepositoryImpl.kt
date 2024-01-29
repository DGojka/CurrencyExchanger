package com.example.currencyexchanger.homescreen.network

import android.accounts.NetworkErrorException
import com.example.currencyexchanger.extensions.inFlow
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
