package com.example.learning_android_virtualwallet_kulakov.ui.models

import com.example.domain.models.Coin

sealed class CoinUiModel(val id: String) {
    data class CoinUI(val coin: Coin, val cryptoComparePrice: Double, val coinCapPrice: Double) : CoinUiModel(coin.id)
    object AddNewCoin : CoinUiModel("")
}
