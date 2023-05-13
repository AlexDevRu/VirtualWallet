package com.example.learning_android_virtualwallet_kulakov.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.models.Coin
import com.example.learning_android_virtualwallet_kulakov.Extensions.collectFlow
import com.example.learning_android_virtualwallet_kulakov.R
import com.example.learning_android_virtualwallet_kulakov.databinding.FragmentMainBinding
import com.example.learning_android_virtualwallet_kulakov.ui.adapters.ConvertedCoinsAdapter
import com.example.learning_android_virtualwallet_kulakov.ui.coin_list.CoinListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), View.OnClickListener, FragmentResultListener, ConvertedCoinsAdapter.Listener {

    private lateinit var binding: FragmentMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private val adapter = ConvertedCoinsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCurrencies.setHasFixedSize(true)
        binding.rvCurrencies.adapter = adapter

        binding.ivCurrency.setOnClickListener(this)

        setFragmentResultListener(CoinListFragment.REQUEST_KEY, ::onFragmentResult)

        binding.etMoney.doAfterTextChanged {
            val value = it?.toString()?.toFloatOrNull() ?: 0f
            viewModel.convertCurrency(value)
        }

        binding.etMoney.setText(viewModel.amount.toString())

        observe()
    }

    private fun observe() {
        collectFlow(viewModel.selectedCoin) { coin ->
            if (coin == null || coin.imageUrl.isNullOrBlank())
                binding.ivCurrency.setImageResource(R.drawable.ic_gold_coin)
            else
                Glide.with(binding.ivCurrency)
                    .load(coin.getFullImageUrl())
                    .error(R.drawable.ic_gold_coin)
                    .into(binding.ivCurrency)
            binding.tlMoney.suffixText = coin?.symbol ?: getString(R.string.usd)
        }
        collectFlow(viewModel.convertedCoins) {
            adapter.submitList(it)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivCurrency -> {
                val action = MainFragmentDirections.actionMainFragmentToCoinListFragment(0)
                findNavController().navigate(action)
            }
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CoinListFragment.REQUEST_KEY) {
            val id = result.getString(CoinListFragment.COIN) ?: return
            val src = result.getInt(CoinListFragment.SRC)
            if (src == 0)
                viewModel.setSelectedCoin(id)
            else
                viewModel.addCoin(id)
        }
    }

    override fun onItemClick(coin: Coin) {

    }

    override fun onRemoveClick(coin: Coin) {
        viewModel.removeCoin(coin.id)
    }

    override fun onAddNewCoin() {
        val action = MainFragmentDirections.actionMainFragmentToCoinListFragment(1)
        findNavController().navigate(action)
    }

}