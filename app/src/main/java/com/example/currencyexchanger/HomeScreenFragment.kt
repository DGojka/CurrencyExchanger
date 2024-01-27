package com.example.currencyexchanger

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.currencyexchanger.databinding.FragmentHomeScreenBinding
import com.example.currencyexchanger.extensions.viewBinding

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private val binding by viewBinding(FragmentHomeScreenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}