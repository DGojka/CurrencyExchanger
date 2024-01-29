package com.example.currencyexchanger.homescreen.network

data class ExchangeRatesJson(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
