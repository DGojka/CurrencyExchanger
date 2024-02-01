package com.example.currencyexchanger.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchanger.homescreen.list.HoldingsRvMapper
import com.example.currencyexchanger.homescreen.managers.HoldingsManager
import com.example.currencyexchanger.homescreen.network.data.ExchangeRates
import com.example.currencyexchanger.homescreen.network.repository.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val repository: ExchangeRatesRepository,
    private val holdingsManager: HoldingsManager,
    private val holdingsRvMapper: HoldingsRvMapper
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(HomeScreenUiState(0.0, mutableListOf(), mutableListOf(), mutableListOf()))
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    private val timer: CountDownTimer = CountDownTimer()
    private lateinit var exchangeRates: ExchangeRates

    private lateinit var currencyToSell: String
    private lateinit var currencyToReceive: String


    fun init() {
        timer.start()
        fetchExchangeRatesEvery5Seconds()
        fetchHoldings()
    }

    fun calculateExchange(value: Double) {
        val exchangeRate = holdingsManager.getHoldings()[currencyToReceive]!!

        val exchangedAmount =
            if (isBaseCurrency()) value * exchangeRate else convertToBaseCurrency(value) * exchangeRate
        _uiState.value = _uiState.value.copy(exchangedCurrencyValue = exchangedAmount)
    }

    fun setCurrencyToSell(selectedCurrency: String) {
        currencyToSell = selectedCurrency
    }

    fun setCurrencyToReceive(selectedCurrency: String) {
        currencyToReceive = selectedCurrency
    }

    fun submitExchange(amount: Double?) {
        val currencyToSellBalance = holdingsManager.getHoldings()[currencyToSell]
        if (currencyToSellBalance != null && amount != null && currencyToSellBalance >= amount) {
            proceedExchange(amount)
        }
    }

    private fun proceedExchange(amount: Double) {

        holdingsManager.saveHolding(
            currencyToSell,
            holdingsManager.getHoldings()[currencyToSell]!! - amount
        )
        holdingsManager.saveHolding(currencyToReceive, _uiState.value.exchangedCurrencyValue)
        fetchHoldings()
    }

    private fun fetchHoldings() {
        val holdings = holdingsManager.getHoldings()
        _uiState.value =
            _uiState.value.copy(
                currenciesHeld = holdings.keys.toList(),
                holdingsRvItem = holdingsRvMapper.mapToHoldingsRv(holdings)
            )
    }


    private fun fetchExchangeRatesEvery5Seconds() {
        //todo surround with try catch and emit error on network/api error
        viewModelScope.launch(Dispatchers.IO) {
            timer.flow.collect { seconds ->
                if (seconds == 0) {
                    exchangeRates = repository.getExchangeRates()
                    _uiState.value =
                        _uiState.value.copy(availableCurrenciesToBuy = exchangeRates.rates.keys.toList())
                    timer.setDuration(5)
                }
            }
        }
    }

    private fun isBaseCurrency() = currencyToSell == exchangeRates.base


    private fun convertToBaseCurrency(value: Double) = value / exchangeRates.rates[currencyToSell]!!


}
