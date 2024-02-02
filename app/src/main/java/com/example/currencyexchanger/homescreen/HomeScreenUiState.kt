package com.example.currencyexchanger.homescreen

import com.example.currencyexchanger.homescreen.data.ExchangeDetails

data class HomeScreenUiState(
    val exchangeDetails: Double,
    val currenciesHeld: List<String>,
    val availableCurrenciesToReceive: List<String>,
    val holdingsRvItem: List<String>,
    val exchangeResult: ExchangeResult? = null
)

sealed class ExchangeResult {
    data class Success(val exchangeDetails: ExchangeDetails) : ExchangeResult()
    object InsufficientBalanceError : ExchangeResult()
    object BlankAmount : ExchangeResult()
    object UnknownNetworkError : ExchangeResult()
}