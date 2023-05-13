package com.example.domain.repositories

import com.example.domain.models.Coin
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllCoins() : List<Coin>
    suspend fun insertCoins(coins: List<Coin>)
    suspend fun getCoinById(id: String) : Coin?
    suspend fun changeObservableCoin(id: String, observable: Boolean)
    fun getObservableCoinsFlow() : Flow<List<Coin>>
    suspend fun updatePrices(coinId: String)
}