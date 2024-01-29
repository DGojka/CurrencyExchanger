package com.example.currencyexchanger.homescreen.network

interface ExchangeRatesMapper {
    fun map(exchangeRatesJson: ExchangeRatesJson): ExchangeRates
}
