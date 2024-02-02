package com.example.currencyexchanger.extensions

fun Double.formatTo2Decimals() = String.format("%.2f", this)

fun Double?.isNullOrZero() = this == null || this == 0.0