package com.example.currencyexchanger.app.di

import android.app.Application
import android.content.Context
import com.example.currencyexchanger.homescreen.managers.AppPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun provideAppContext(): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun provideAppPreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }

}