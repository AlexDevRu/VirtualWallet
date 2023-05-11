package com.example.learning_android_virtualwallet_kulakov.ui.main

import androidx.lifecycle.ViewModel
import com.example.domain.use_cases.GetAllCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllCoinsUseCase: GetAllCoinsUseCase
) : ViewModel() {

    fun convertCurrency(amount: Double) {

    }

}