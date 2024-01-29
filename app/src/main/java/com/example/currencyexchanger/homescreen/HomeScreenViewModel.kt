package com.example.currencyexchanger.homescreen

import androidx.lifecycle.ViewModel
import com.example.currencyexchanger.homescreen.network.repository.ExchangeRatesRepository

class HomeScreenViewModel(private val repository: ExchangeRatesRepository) : ViewModel()
