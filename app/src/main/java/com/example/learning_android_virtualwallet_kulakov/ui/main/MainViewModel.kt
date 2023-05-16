package com.example.learning_android_virtualwallet_kulakov.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.domain.use_cases.ChangeObservableCoinUseCase
import com.example.domain.use_cases.GetLocalCoinByIdUseCase
import com.example.domain.use_cases.GetObservableCoinsFlowUseCase
import com.example.domain.use_cases.UpdatePricesUseCase
import com.example.domain.utils.SharedPrefs
import com.example.learning_android_virtualwallet_kulakov.ui.Utils
import com.example.learning_android_virtualwallet_kulakov.ui.models.CoinUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getLocalCoinByIdUseCase: GetLocalCoinByIdUseCase,
    private val sharedPrefs: SharedPrefs,
    private val changeObservableCoinUseCase: ChangeObservableCoinUseCase,
    private val getObservableCoinsFlowUseCase: GetObservableCoinsFlowUseCase,
    private val updatePricesUseCase: UpdatePricesUseCase,
    app: Application
) : ViewModel() {

    private val _selectedCoinId = MutableStateFlow<String?>(null)

    private val _amount = MutableStateFlow(0f)

    val amount: Float
        get() = _amount.value

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedCoin = _selectedCoinId.flatMapLatest {
        if (it.isNullOrBlank())
            flowOf(null)
        else
            getLocalCoinByIdUseCase.getFlow(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val convertedCoins = selectedCoin.flatMapLatest { currentCoin ->
        Log.d("asd", "currentCoin: $currentCoin")
        _amount.flatMapLatest { amount ->
            Log.d("asd", "amount: $amount")
            getObservableCoinsFlowUseCase().map {
                val currentSumInUsd1 = amount * (currentCoin?.cryptoComparePrice?.toFloat() ?: 1f)
                val currentSumInUsd2 = amount * (currentCoin?.coinCapPrice?.toFloat() ?: 1f)
                Log.d("asd", "currentSumInUsd1: $currentSumInUsd1")
                Log.d("asd", "currentSumInUsd2: $currentSumInUsd2")
                it.map {
                    val cryptoComparePrice = if (it.cryptoComparePrice > 0) currentSumInUsd1 / it.cryptoComparePrice else -1.0
                    val coinCapPrice = if (it.coinCapPrice > 0) currentSumInUsd2 / it.coinCapPrice else -1.0
                    Log.d("asd", "cryptoComparePrice: $cryptoComparePrice")
                    Log.d("asd", "coinCapPrice: $coinCapPrice")
                    CoinUiModel.CoinUI(it, cryptoComparePrice, coinCapPrice)
                } + CoinUiModel.AddNewCoin
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    init {
        viewModelScope.launch { _selectedCoinId.emit(sharedPrefs.selectedCoinId) }
        viewModelScope.launch { _amount.emit(sharedPrefs.coinAmount) }

        Utils.startWorker(app, ExistingPeriodicWorkPolicy.KEEP, 12, 12)
    }

    fun setSelectedCoin(coinId: String) {
        sharedPrefs.selectedCoinId = coinId
        viewModelScope.launch {
            _selectedCoinId.emit(coinId)
            if (coinId.isNotBlank())
                updatePricesUseCase(coinId)
        }
    }

    fun convertCurrency(amount: Float) {
        sharedPrefs.coinAmount = amount
        viewModelScope.launch { _amount.emit(amount) }
    }

    fun addCoin(id: String) {
        viewModelScope.launch {
            changeObservableCoinUseCase(id, true)
        }
    }

    fun removeCoin(id: String) {
        viewModelScope.launch {
            changeObservableCoinUseCase(id, false)
        }
    }

}