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
import com.example.learning_android_virtualwallet_kulakov.databinding.ListItemAddCurrencyBinding
import com.example.learning_android_virtualwallet_kulakov.databinding.ListItemCurrencyBinding
import com.example.learning_android_virtualwallet_kulakov.ui.models.CoinUiModel

class ConvertedCoinsAdapter(
    private val listener: Listener
): ListAdapter<CoinUiModel, ConvertedCoinsAdapter.BaseViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : ItemCallback<CoinUiModel>() {
            override fun areItemsTheSame(oldItem: CoinUiModel, newItem: CoinUiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CoinUiModel, newItem: CoinUiModel): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: CoinUiModel, newItem: CoinUiModel): Any? {
                return if (oldItem is CoinUiModel.CoinUI && newItem is CoinUiModel.CoinUI)
                    oldItem.cryptoComparePrice != newItem.cryptoComparePrice || oldItem.coinCapPrice != newItem.coinCapPrice
                else
                    null
            }
        }
    }

    interface Listener {
        fun onItemClick(coin: Coin)
        fun onAddNewCoin()
        fun onRemoveClick(coin: Coin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            0 -> {
                val binding = ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CoinViewHolder(binding)
            }
            else -> {
                val binding = ListItemAddCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AddCoinViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CoinUiModel.CoinUI -> 0
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads.first() == false)
            (holder as? CoinViewHolder)?.bindPrice(getItem(position))
        else
            super.onBindViewHolder(holder, position, payloads)
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(coin: CoinUiModel) {}
    }

    inner class CoinViewHolder(
        private val binding: ListItemCurrencyBinding
    ) : BaseViewHolder(binding.root), View.OnClickListener {

        private var coin: Coin? = null

        init {
            binding.root.setOnClickListener(this)
            binding.btnClose.setOnClickListener(this)
        }

        override fun bind(coin: CoinUiModel) {
            if (coin !is CoinUiModel.CoinUI) return
            this.coin = coin.coin

            binding.tvCurrency.text = coin.coin.fullName

            if (coin.coin.imageUrl.isNullOrBlank())
                binding.ivCurrency.setImageResource(R.drawable.ic_gold_coin)
            else
                Glide.with(binding.ivCurrency)
                    .load(coin.coin.getFullImageUrl())
                    .error(R.drawable.ic_gold_coin)
                    .into(binding.ivCurrency)

            bindPrice(coin)
        }

        fun bindPrice(coin: CoinUiModel) {
            if (coin !is CoinUiModel.CoinUI) return

            if (coin.cryptoComparePrice < 0)
                binding.tvCryptoComparePrice.setText(R.string.data_is_unavailable)
            else
                binding.tvCryptoComparePrice.text = String.format("%.2f", coin.cryptoComparePrice)

            if (coin.coinCapPrice < 0)
                binding.tvCoinCapPrice.setText(R.string.data_is_unavailable)
            else
                binding.tvCoinCapPrice.text = String.format("%.2f", coin.coinCapPrice)
        }

        override fun onClick(view: View?) {
            when (view) {
                binding.root -> coin?.let { listener.onItemClick(it) }
                binding.btnClose -> coin?.let { listener.onRemoveClick(it) }
            }
        }
    }

    inner class AddCoinViewHolder(
        private val binding: ListItemAddCurrencyBinding
    ) : BaseViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view) {
                binding.root -> listener.onAddNewCoin()
            }
        }

    }
}