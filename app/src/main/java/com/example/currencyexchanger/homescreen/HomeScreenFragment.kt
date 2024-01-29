package com.example.currencyexchanger.homescreen

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.currencyexchanger.R
import com.example.currencyexchanger.app.di.AppComponent.Companion.appComponent
import com.example.currencyexchanger.databinding.FragmentHomeScreenBinding
import com.example.currencyexchanger.extensions.daggerViewModel
import com.example.currencyexchanger.extensions.viewBinding
import javax.inject.Inject
import javax.inject.Provider

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    @Inject
    lateinit var viewModelProvider: Provider<HomeScreenViewModel>

    private val binding by viewBinding(FragmentHomeScreenBinding::bind)
    private val viewModel by daggerViewModel { viewModelProvider }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
