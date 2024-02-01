package com.example.currencyexchanger.app

import android.app.Application
import com.example.currencyexchanger.app.di.AppComponent
import com.example.currencyexchanger.app.di.AppModule
import com.example.currencyexchanger.app.di.DaggerAppComponent
import com.example.currencyexchanger.homescreen.managers.AppPreferences
import com.example.currencyexchanger.homescreen.managers.HoldingsManager
import javax.inject.Inject

class CurrencyExchangerApp : Application() {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var holdingsManager: HoldingsManager

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
        setupFirstAppLaunch()
        if (appPreferences.isFirstTime()) {
            setupFirstAppLaunch()
        }
    }

    private fun setupFirstAppLaunch() {
        appPreferences.setFirstTime(false)
        appPreferences.setFreeExchangesAmount(FREE_EXCHANGES_AMOUNT)
        holdingsManager.saveHolding(BASE_HOLDING, STARTING_BALANCE)
    }

    companion object {
        private const val FREE_EXCHANGES_AMOUNT = 5
        private const val BASE_HOLDING = "EUR"
        private const val STARTING_BALANCE = 1000.00
    }
}

