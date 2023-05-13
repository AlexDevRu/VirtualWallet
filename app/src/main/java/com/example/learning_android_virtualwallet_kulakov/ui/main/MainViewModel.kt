package com.example.learning_android_virtualwallet_kulakov.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.use_cases.ChangeObservableCoinUseCase
import com.example.domain.use_cases.GetLocalCoinByIdUseCase
import com.example.domain.use_cases.GetObservableCoinsFlowUseCase
import com.example.domain.use_cases.UpdatePricesUseCase
import com.example.domain.utils.SharedPrefs
import com.example.learning_android_virtualwallet_kulakov.ui.models.CoinUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val updatePricesUseCase: UpdatePricesUseCase
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
                val currentSumInUsd = amount * (currentCoin?.price?.toFloat() ?: 1f)
                Log.d("asd", "currentSumInUsd: $currentSumInUsd")
                it.map {
                    val amountPrice = currentSumInUsd / it.price
                    Log.d("asd", "amountPrice: $amountPrice")
                    CoinUiModel.CoinUI(it, amountPrice)
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
    }

    fun setSelectedCoin(coinId: String) {
        sharedPrefs.selectedCoinId = coinId
        viewModelScope.launch {
            _selectedCoinId.emit(coinId)
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