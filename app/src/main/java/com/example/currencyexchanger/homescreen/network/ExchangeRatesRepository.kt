package com.example.currencyexchanger.homescreen.network

interface ExchangeRatesRepository {
    suspend fun getExchangeRates() : ExchangeRates
}
