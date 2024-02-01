package com.example.currencyexchanger.homescreen.managers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HoldingsManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(HOLDINGS_PREFS, Context.MODE_PRIVATE)
    private val gson = Gson()


    fun saveHolding(currency: String, newValue: Double) {
        val holdings = getHoldings().toMutableMap()
        holdings[currency] = newValue
        saveHoldings(holdings)
    }

    fun getHoldings(): Map<String, Double> {
        val json = sharedPreferences.getString(HOLDINGS, "")
        if (json.isNullOrEmpty()) {
            return emptyMap()
        }
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(json, type)
    }


    private fun saveHoldings(holdings: Map<String, Double>) {
        val json = gson.toJson(holdings)
        sharedPreferences.edit().putString(HOLDINGS, json).apply()
    }

    companion object {
        private const val HOLDINGS_PREFS = "HoldingsPreferences"
        private const val HOLDINGS = "holdings"
    }
}