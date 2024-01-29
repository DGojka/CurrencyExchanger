package com.example.currencyexchanger.app

import android.app.Application
import com.example.currencyexchanger.app.di.AppComponent
import com.example.currencyexchanger.app.di.DaggerAppComponent

class CurrencyExchangerApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .build()
    }
}
