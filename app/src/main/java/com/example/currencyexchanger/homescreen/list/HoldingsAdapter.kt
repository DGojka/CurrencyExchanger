package com.example.currencyexchanger.homescreen.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchanger.databinding.HoldingsItemBinding
import com.example.currencyexchanger.extensions.viewBinding

class HoldingsAdapter :
    ListAdapter<String, HoldingsAdapter.HoldingsViewHolder>(HoldingsDiffCallback()) {

    class HoldingsViewHolder(private val binding: HoldingsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(holding: String) {
            binding.holding.text = holding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingsViewHolder =
        HoldingsViewHolder(parent.viewBinding(HoldingsItemBinding::inflate))

    override fun onBindViewHolder(holder: HoldingsViewHolder, position: Int) =
        holder.bind(getItem(position))
}
