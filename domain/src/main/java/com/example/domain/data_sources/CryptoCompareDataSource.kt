package com.example.domain.data_sources

import com.example.domain.models.Coin

interface CryptoCompareDataSource {
    suspend fun getAllCoins() : List<Coin>
}