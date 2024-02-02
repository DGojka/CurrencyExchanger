package com.example.currencyexchanger.homescreen.list

interface HoldingsRvMapper {
    fun mapToHoldingsRv(balances: Map<String, Double>): List<String>
}