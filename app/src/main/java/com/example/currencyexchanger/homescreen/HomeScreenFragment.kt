package com.example.currencyexchanger.homescreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexchanger.R
import com.example.currencyexchanger.app.di.AppComponent.Companion.appComponent
import com.example.currencyexchanger.databinding.FragmentHomeScreenBinding
import com.example.currencyexchanger.extensions.daggerViewModel
import com.example.currencyexchanger.extensions.formatTo2Decimals
import com.example.currencyexchanger.extensions.viewBinding
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
        viewModel.init()
        setupHoldingsRv()
        setupSellAmountTextChangeListener()
        setupCurrencyPickers()
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
        binding.submitButton.setOnClickListener {
            viewModel.submitExchange(binding.sellAmount.text.toString().toDoubleOrNull())
        }
        when (val result = state.exchangeResult) {
            is ExchangeResult.Success -> {
                with(result.exchangeDetails) {
                    Toast.makeText(
                        requireContext(),
                        "Sold ${holdingToSell.amount.formatTo2Decimals()} ${holdingToSell.currency} and received ${holdingToReceive.amount.formatTo2Decimals()} ${holdingToReceive.currency}, fee: ${fee.formatTo2Decimals()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            ExchangeResult.InsufficientBalanceError -> Toast.makeText(
                requireContext(),
                "Insufficient balance",
                Toast.LENGTH_SHORT
            ).show()
            ExchangeResult.UnknownNetworkError -> Toast.makeText(
                requireContext(),
                "Network error",
                Toast.LENGTH_SHORT
            ).show()
            null -> {}
        }
    }

    private fun setupSellAmountTextChangeListener() {
        binding.sellAmount.addTextChangedListener(getOnTextChangeListener {
            viewModel.notifyExchangeDataChanged(it.toDouble())
        })
    }

    private fun setupCurrencyPickers() {
        sellCurrencyAdapter = setupSpinnerAdapter()
        receiveCurrencyAdapter = setupSpinnerAdapter()

        sellCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        receiveCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sellCurrencySpinner.adapter = sellCurrencyAdapter
        binding.receiveCurrencySpinner.adapter = receiveCurrencyAdapter
        binding.sellCurrencySpinner.onItemSelectedListener =
            getOnItemSelectedListener {
                viewModel.setCurrencyToSell(it)
                val currentAmount = binding.sellAmount.text.toString()
                if (currentAmount.isNotBlank()) {
                    viewModel.notifyExchangeDataChanged(
                        binding.sellAmount.text.toString().toDouble()
                    )
                }
            }
        binding.receiveCurrencySpinner.onItemSelectedListener =
            getOnItemSelectedListener {
                viewModel.setCurrencyToReceive(it)
                val currentAmount = binding.sellAmount.text.toString()
                if (currentAmount.isNotBlank()) {
                    viewModel.notifyExchangeDataChanged(currentAmount.toDouble())
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

