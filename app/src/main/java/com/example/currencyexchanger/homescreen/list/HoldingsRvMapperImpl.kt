package com.example.currencyexchanger.homescreen.list

import com.example.currencyexchanger.extensions.formatTo2Decimals

class HoldingsRvMapperImpl : HoldingsRvMapper {
    override fun mapToHoldingsRv(balances: Map<String, Double>): List<String> =
        balances.entries.map {
            "${it.value.formatTo2Decimals()} ${it.key}"
        }
}