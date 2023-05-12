package com.example.domain.data_sources

import com.example.domain.models.Coin

interface LocalDataSource {
    suspend fun insertCoins(coins: List<Coin>)
    suspend fun getAllCoins() : List<Coin>
    suspend fun getCoinById(id: String) : Coin?
}