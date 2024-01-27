package com.example.currencyexchanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.currencyexchanger.databinding.ActivityMainBinding
import com.example.currencyexchanger.extensions.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}