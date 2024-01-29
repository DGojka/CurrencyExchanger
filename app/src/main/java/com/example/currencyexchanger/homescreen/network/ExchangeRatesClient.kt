package com.example.currencyexchanger.homescreen.network

import com.example.currencyexchanger.homescreen.network.data.ExchangeRatesJson
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRatesClient {
    @GET("/currency-exchange-rates")
    suspend fun getExchangeRates(): Response<ExchangeRatesJson>
}
