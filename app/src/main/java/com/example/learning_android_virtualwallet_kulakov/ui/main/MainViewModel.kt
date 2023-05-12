package com.example.learning_android_virtualwallet_kulakov.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Coin
import com.example.domain.use_cases.GetAllCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCoinsUseCase: GetAllCoinsUseCase
) : ViewModel() {

    private val _selectedCoin = MutableStateFlow<Coin?>(null)
    val selectedCoin = _selectedCoin.asStateFlow()

    fun setSelectedCoin(coinId: String) {
        viewModelScope.launch {

        }
    }

    fun convertCurrency(amount: Double) {

    }

}