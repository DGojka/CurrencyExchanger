package com.example.currencyexchanger.homescreen.network.repository

import com.example.currencyexchanger.homescreen.network.data.ExchangeRates

interface ExchangeRatesRepository {
    suspend fun getExchangeRates() : ExchangeRates
}
