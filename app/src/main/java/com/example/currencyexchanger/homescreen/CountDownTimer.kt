package com.example.currencyexchanger.homescreen

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountDownTimer {

    private var _counterFlow: Flow<Int>? = null
    var flow: MutableStateFlow<Int> = MutableStateFlow(0)
    private var currentMs: Int = 0

    fun start() {
        _counterFlow = (0..Int.MAX_VALUE)
            .asSequence()
            .asFlow()
            .onEach {
                delay(ONE_SECOND)
                currentMs -= ONE_SECOND.toInt()
                flow.emit(currentMs)
            }
        GlobalScope.launch {
            _counterFlow!!.collect {}
        }
    }

    fun setDuration(seconds: Int) {
        flow.value = seconds * 1000
        currentMs = seconds * 1000
    }

    companion object {
        private const val ONE_SECOND = 1_000L
    }
}