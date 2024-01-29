package com.example.currencyexchanger.homescreen.network.mappers

import com.example.currencyexchanger.homescreen.network.data.ExchangeRates
import com.example.currencyexchanger.homescreen.network.data.ExchangeRatesJson

class ExchangeRatesMapperImpl : ExchangeRatesMapper {
    override fun map(exchangeRatesJson: ExchangeRatesJson): ExchangeRates {
        with(exchangeRatesJson) {
            return ExchangeRates(base, rates)
        }
    }
}
