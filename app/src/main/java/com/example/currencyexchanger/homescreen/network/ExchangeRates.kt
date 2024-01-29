package com.example.currencyexchanger.homescreen.network

data class ExchangeRates(val base: String, val rates: Map<String, Double>)
