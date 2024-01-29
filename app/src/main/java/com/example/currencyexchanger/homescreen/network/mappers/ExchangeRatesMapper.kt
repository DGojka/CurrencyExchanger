package com.example.currencyexchanger.homescreen.network.mappers

import com.example.currencyexchanger.homescreen.network.data.ExchangeRates
import com.example.currencyexchanger.homescreen.network.data.ExchangeRatesJson

interface ExchangeRatesMapper {
    fun map(exchangeRatesJson: ExchangeRatesJson): ExchangeRates
}
