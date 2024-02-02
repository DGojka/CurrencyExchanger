package com.example.currencyexchanger.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchanger.extensions.isNullOrZero
import com.example.currencyexchanger.homescreen.data.Balance
import com.example.currencyexchanger.homescreen.data.ExchangeDetails
import com.example.currencyexchanger.homescreen.list.HoldingsRvMapper
import com.example.currencyexchanger.homescreen.managers.AppPreferences
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
    private val holdingsRvMapper: HoldingsRvMapper,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(HomeScreenUiState(0.0, mutableListOf(), mutableListOf(), mutableListOf()))
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    private val timer: CountDownTimer = CountDownTimer()
    private var firstExchangeRateFetched = false

    private lateinit var exchangeRates: ExchangeRates

    private lateinit var currencyToSell: String
    private lateinit var currencyToReceive: String

    init {
        fetchExchangeRatesEvery5Seconds()
        fetchHoldings()
    }

    fun notifyExchangeDataChanged(value: Double) {
        _uiState.value =
            _uiState.value.copy(
                exchangeDetails = calculateExchange(value).holdingToReceive.amount,
                exchangeResult = null
            )
    }

    fun submitExchange(amount: Double?) {
        val currencyToSellBalance = holdingsManager.getHoldings()[currencyToSell]
        if (requirementsToExchangeMet(currencyToSellBalance, amount)) {
            proceedExchange(amount!!)
        } else if (amount.isNullOrZero()) {
            _uiState.value =
                _uiState.value.copy(exchangeResult = ExchangeResult.BlankAmount)
        } else {
            _uiState.value =
                _uiState.value.copy(exchangeResult = ExchangeResult.InsufficientBalanceError)
        }
    }

    fun setCurrencyToSell(selectedCurrency: String) {
        currencyToSell = selectedCurrency
    }

    fun setCurrencyToReceive(selectedCurrency: String) {
        if (::currencyToReceive.isInitialized) {
            val currenciesToSell =
                holdingsManager.getHoldings().keys.minus(selectedCurrency).toList()
            _uiState.value = _uiState.value.copy(
                currenciesHeld = currenciesToSell,
                exchangeResult = null
            )
            if (currencyToSell == selectedCurrency) {
                currencyToSell = currenciesToSell[0]
            }
        }

        currencyToReceive = selectedCurrency
    }


    private fun calculateExchange(value: Double): ExchangeDetails {
        val exchangeRate = exchangeRates.rates[currencyToReceive]!!
        var fee = 0.0
        if (!freeExchangesAvailable()) {
            fee = calculateFee(value)
        }
        val amountWithFee = value - fee
        val exchangedAmount = when {
            isCurrencyToSellBaseCurrency() -> amountWithFee * exchangeRate
            isCurrencyToReceiveBaseCurrency() -> {
                val rate = exchangeRates.rates[currencyToSell]!!
                amountWithFee / rate
            }
            else -> convertToBaseCurrency(amountWithFee) * exchangeRate
        }

        return ExchangeDetails(
            Balance(currencyToSell, value),
            Balance(currencyToReceive, exchangedAmount),
            fee
        )
    }

    private fun proceedExchange(amount: Double) {
        val exchangeDetails = calculateExchange(amount)
        if (freeExchangesAvailable()) {
            appPreferences.useFreeExchange()
        }
        holdingsManager.sellHolding(
            currencyToSell,
            amount
        )
        holdingsManager.receiveHolding(
            exchangeDetails.holdingToReceive.currency,
            exchangeDetails.holdingToReceive.amount
        )
        _uiState.value =
            _uiState.value.copy(exchangeResult = ExchangeResult.Success(exchangeDetails))
        fetchHoldings()
    }

    private fun fetchHoldings() {
        val holdings = holdingsManager.getHoldings()
        _uiState.value =
            _uiState.value.copy(
                currenciesHeld = holdings.keys.toList(),
                holdingsRvItem = holdingsRvMapper.mapToHoldingsRv(holdings),
                exchangeResult = null
            )
    }

    private fun fetchExchangeRatesEvery5Seconds() {
        timer.start()
        viewModelScope.launch(Dispatchers.IO) {
            timer.flow.collect { seconds ->
                if (seconds == 0) {
                    try {
                        exchangeRates = repository.getExchangeRates()
                        if (!firstExchangeRateFetched) {
                            _uiState.value =
                                _uiState.value.copy(availableCurrenciesToReceive = exchangeRates.rates.keys.toList())
                            firstExchangeRateFetched = true
                        }
                    } catch (e: Exception) {
                        _uiState.value =
                            _uiState.value.copy(exchangeResult = ExchangeResult.UnknownNetworkError)
                    }
                    timer.setDuration(5)
                }
            }
        }
    }

    private fun isCurrencyToSellBaseCurrency() = currencyToSell == exchangeRates.base

    private fun isCurrencyToReceiveBaseCurrency() = currencyToReceive == exchangeRates.base

    private fun convertToBaseCurrency(value: Double) = value / exchangeRates.rates[currencyToSell]!!

    private fun calculateFee(amount: Double) = amount * commissionFee

    private fun freeExchangesAvailable(): Boolean = appPreferences.getFreeExchangesAmount() > 0


    private fun requirementsToExchangeMet(
        currencyToSellBalance: Double?,
        amount: Double?
    ): Boolean =
        currencyToSellBalance != null && !amount.isNullOrZero() && isAmountGreaterThanBalance(
            currencyToSellBalance,
            amount!!
        )

    private fun isAmountGreaterThanBalance(
        currencyToSellBalance: Double,
        amount: Double
    ) = currencyToSellBalance >= amount

    companion object {
        private const val commissionFee = 0.007
    }
}
