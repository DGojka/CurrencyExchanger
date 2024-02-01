package com.example.currencyexchanger.homescreen

data class HomeScreenUiState(
    val exchangedCurrencyValue: Double,
    val currenciesHeld: List<String>,
    val availableCurrenciesToBuy: List<String>,
    val holdingsRvItem: List<String>
)