package com.example.currencyexchanger.homescreen.network

class ExchangeRatesMapperImpl : ExchangeRatesMapper {
    override fun map(exchangeRatesJson: ExchangeRatesJson): ExchangeRates {
        with(exchangeRatesJson) {
            return ExchangeRates(base, rates)
        }
    }
}
