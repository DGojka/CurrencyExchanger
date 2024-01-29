package com.example.currencyexchanger.app.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.example.currencyexchanger.app.CurrencyExchangerApp
import com.example.currencyexchanger.homescreen.HomeScreenFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DataModule::class, HomeScreenModule::class])
interface AppComponent {

    fun inject(homeScreenFragment: HomeScreenFragment)

    companion object {
        val Activity.appComponent: AppComponent
            get() = (application as CurrencyExchangerApp).appComponent

        val Fragment.appComponent: AppComponent
            get() = requireActivity().appComponent
    }
}
