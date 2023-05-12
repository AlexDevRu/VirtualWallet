package com.example.learning_android_virtualwallet_kulakov.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.learning_android_virtualwallet_kulakov.R
import com.example.learning_android_virtualwallet_kulakov.databinding.FragmentMainBinding
import com.example.learning_android_virtualwallet_kulakov.ui.coin_list.CoinListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), FragmentResultListener {

    private lateinit var binding: FragmentMainBinding

    private val viewModel by viewModels<MainViewModel>()

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
        binding.ivCurrency.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCoinListFragment()
            findNavController().navigate(action)
        }

        parentFragmentManager.setFragmentResultListener(CoinListFragment.COIN, this, this)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        if (requestKey == CoinListFragment.COIN) {
            val id = result.getString(CoinListFragment.COIN) ?: return
            viewModel.setSelectedCoin(id)
        }
    }

}