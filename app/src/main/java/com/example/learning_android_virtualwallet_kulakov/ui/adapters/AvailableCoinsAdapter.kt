package com.example.learning_android_virtualwallet_kulakov.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.models.Coin
import com.example.learning_android_virtualwallet_kulakov.R
import com.example.learning_android_virtualwallet_kulakov.databinding.ListItemAvailableCoinBinding

class AvailableCoinsAdapter(
    private val listener: Listener
): ListAdapter<Coin, AvailableCoinsAdapter.CoinViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Listener {
        fun onItemClick(coin: Coin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ListItemAvailableCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CoinViewHolder(
        private val binding: ListItemAvailableCoinBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var coin: Coin? = null

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(coin: Coin) {
            this.coin = coin
            binding.tvCurrency.text = coin.fullName
            if (coin.imageUrl.isNullOrBlank())
                binding.ivCurrency.setImageResource(R.drawable.ic_gold_coin)
            else
                Glide.with(binding.ivCurrency)
                    .load(coin.getFullImageUrl())
                    .error(R.drawable.ic_gold_coin)
                    .into(binding.ivCurrency)
        }

        override fun onClick(view: View?) {
            when (view) {
                binding.root -> coin?.let { listener.onItemClick(it) }
            }
        }
    }
}