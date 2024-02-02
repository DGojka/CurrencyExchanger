package com.example.currencyexchanger.homescreen.managers

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    fun isFirstTime(): Boolean {
        return preferences.getBoolean(FIRST_TIME_APP_USED_KEY, true)
    }

    fun setFirstTime() {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean(FIRST_TIME_APP_USED_KEY, false)
        editor.apply()
    }

    fun setFreeExchangesAmount(amount: Int) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt(FREE_EXCHANGES, amount)
        editor.apply()
    }

    fun getFreeExchangesAmount(): Int = preferences.getInt(FREE_EXCHANGES, 0)

    fun useFreeExchange() =  setFreeExchangesAmount(getFreeExchangesAmount() - 1)


    companion object {
        private const val SETTINGS = "Settings"
        private const val FIRST_TIME_APP_USED_KEY = "firstTime"
        private const val FREE_EXCHANGES = "freeExchanges"
    }
}