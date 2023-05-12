package com.example.learning_android_virtualwallet_kulakov.ui.coin_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Coin
import com.example.learning_android_virtualwallet_kulakov.Extensions.collectFlow
import com.example.learning_android_virtualwallet_kulakov.databinding.FragmentCoinListBinding
import com.example.learning_android_virtualwallet_kulakov.ui.adapters.AvailableCoinsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CoinListFragment : Fragment(), AvailableCoinsAdapter.Listener, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentCoinListBinding

    private val viewModel by viewModels<CoinListViewModel>()

    private val adapter = AvailableCoinsAdapter(this)

    private val args by navArgs<CoinListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCurrencies.setHasFixedSize(true)
        binding.rvCurrencies.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.rvCurrencies.addItemDecoration(dividerItemDecoration)

        binding.svSearch.setOnQueryTextListener(this)

        observe()
    }

    private fun observe() {
        collectFlow(viewModel.coins) {
            adapter.submitList(it)
        }
        collectFlow(viewModel.loading) {
            binding.progressBar.isVisible = it
        }
    }

    override fun onItemClick(coin: Coin) {
        setFragmentResult(REQUEST_KEY, bundleOf(COIN to coin.id, SRC to args.src))
        findNavController().popBackStack()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?) = false

    companion object {
        const val REQUEST_KEY = "CoinListFragment"
        const val COIN = "COIN"
        const val SRC = "SRC"
    }
}