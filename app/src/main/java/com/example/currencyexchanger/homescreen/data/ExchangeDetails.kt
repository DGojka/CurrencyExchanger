package com.example.currencyexchanger.homescreen.data

data class ExchangeDetails(
    val holdingToSell: Balance,
    val holdingToReceive: Balance,
    val fee: Double
)

data class Balance(val currency: String, val amount: Double)

