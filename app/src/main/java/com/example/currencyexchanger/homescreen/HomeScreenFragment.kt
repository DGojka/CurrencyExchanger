package com.example.currencyexchanger.homescreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexchanger.R
import com.example.currencyexchanger.app.di.AppComponent.Companion.appComponent
import com.example.currencyexchanger.databinding.FragmentHomeScreenBinding
import com.example.currencyexchanger.extensions.*
import com.example.currencyexchanger.homescreen.list.HoldingsAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    @Inject
    lateinit var viewModelProvider: Provider<HomeScreenViewModel>

    private val binding by viewBinding(FragmentHomeScreenBinding::bind)
    private val viewModel by daggerViewModel { viewModelProvider }

    private lateinit var holdingsAdapter: HoldingsAdapter
    private lateinit var sellCurrencyAdapter: ArrayAdapter<String>
    private lateinit var receiveCurrencyAdapter: ArrayAdapter<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHoldingsRv()
        setupSellAmountTextChangeListener()
        setupCurrencyPickers()
        setupSubmitButton()
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                bindState(state)
            }
        }
    }

    private fun bindState(state: HomeScreenUiState) {
        holdingsAdapter.submitList(state.holdingsRvItem)
        binding.receiveAmount.text =
            "+${state.exchangeDetails.formatTo2Decimals()}"
        updateAvailableCurrencies(state.availableCurrenciesToReceive)
        updateAvailableCurrenciesToSell(state.currenciesHeld)
        handleExchangeResult(state.exchangeResult)
    }

    private fun handleExchangeResult(exchangeResult: ExchangeResult?) {
        when (exchangeResult) {
            is ExchangeResult.Success -> {
                with(exchangeResult.exchangeDetails) {
                    requireContext().showLongToast(
                        getString(
                            R.string.exchange_success_toast,
                            holdingToSell.amount.formatTo2Decimals(),
                            holdingToSell.currency,
                            holdingToReceive.amount.formatTo2Decimals(),
                            holdingToReceive.currency,
                            fee.formatTo2Decimals()
                        )
                    )
                }
            }
            ExchangeResult.InsufficientBalanceError -> requireContext().showShortToast(getString(R.string.insufficient_balance_error_toast))
            ExchangeResult.UnknownNetworkError -> requireContext().showShortToast(getString(R.string.unknown_network_error_toast))
            ExchangeResult.BlankAmount -> requireContext().showShortToast(getString(R.string.blank_amount_toast))
            null -> {}
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.submitExchange(
                binding.sellAmountEditText.text.toString().toDoubleOrNull(),
                currencyToReceive = binding.receiveCurrencySpinner.selectedItem.toString(),
                currencyToSell = binding.sellCurrencySpinner.selectedItem.toString()
            )
        }
    }

    private fun setupSellAmountTextChangeListener() {
        binding.sellAmountEditText.addTextChangedListener(getOnTextChangeListener {
            viewModel.calculateExchange(
                it.toDouble(),
                currencyToReceive = binding.receiveCurrencySpinner.selectedItem.toString(),
                binding.sellCurrencySpinner.selectedItem.toString()
            )
        })
    }

    private fun setupCurrencyPickers() {
        sellCurrencyAdapter = setupSpinnerAdapter()
        receiveCurrencyAdapter = setupSpinnerAdapter()

        sellCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        receiveCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(binding) {
            sellCurrencySpinner.adapter = sellCurrencyAdapter
            receiveCurrencySpinner.adapter = receiveCurrencyAdapter
            sellCurrencySpinner.onItemSelectedListener =
                getOnItemSelectedListener {
                    val currentAmount = sellAmountEditText.text.toString()
                    if (currentAmount.isNotBlank()) {
                        viewModel.calculateExchange(
                            currentAmount.toDouble(),
                            currencyToReceive = receiveCurrencySpinner.selectedItem.toString(),
                            it
                        )
                    }
                }
            receiveCurrencySpinner.onItemSelectedListener =
                getOnItemSelectedListener {
                    val currentAmount = sellAmountEditText.text.toString()
                    if (currentAmount.isNotBlank()) {
                        viewModel.calculateExchange(
                            currentAmount.toDouble(),
                            currencyToReceive = it,
                            currencyToSell = sellCurrencySpinner.selectedItem.toString()
                        )
                    }
                }
        }
    }

    private fun getOnTextChangeListener(onTextChange: (text: String) -> Unit) =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    onTextChange(s.toString())

                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

    private fun getOnItemSelectedListener(onItemSelected: (selectedItem: String) -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCurrency = parent?.getItemAtPosition(position).toString()
                onItemSelected(selectedCurrency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupHoldingsRv() {
        binding.balances.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        holdingsAdapter = HoldingsAdapter()
        binding.balances.adapter = holdingsAdapter
    }

    private fun setupSpinnerAdapter() = ArrayAdapter(
        requireContext(),
        android.R.layout.simple_spinner_item,
        mutableListOf<String>()
    )

    private fun updateAvailableCurrencies(currencies: List<String>) {
        receiveCurrencyAdapter.clear()
        receiveCurrencyAdapter.addAll(currencies)
    }

    private fun updateAvailableCurrenciesToSell(keys: List<String>) {
        sellCurrencyAdapter.clear()
        sellCurrencyAdapter.addAll(keys)
    }
}

