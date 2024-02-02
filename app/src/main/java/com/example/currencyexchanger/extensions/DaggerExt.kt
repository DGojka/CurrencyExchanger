package com.example.currencyexchanger.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

inline fun <reified VM : ViewModel> AppCompatActivity.daggerViewModel(
    crossinline getProvider: () -> Provider<VM>
) = lazy {
    ViewModelProvider(this, getProvider().createFactory())[VM::class.java]
}

inline fun <reified VM : ViewModel> Fragment.daggerViewModel(
    crossinline getProvider: () -> Provider<VM>
) = lazy {
    ViewModelProvider(this, getProvider().createFactory())[VM::class.java]
}

inline fun <reified VM : ViewModel> Provider<VM>.createFactory() =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return this@createFactory.get() as T
        }
    }
