package com.example.domain.data_sources

import com.example.domain.models.Coin
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertCoins(coins: List<Coin>)
    suspend fun getAllCoins() : List<Coin>
    suspend fun getCoinById(id: String) : Coin?
    fun getCoinByIdFlow(id: String) : Flow<Coin?>
    suspend fun changeObservableCoin(id: String, observable: Boolean)
    fun getObservableCoinsFlow() : Flow<List<Coin>>
    suspend fun updatePrice(id: String, price: Double)
    suspend fun getObservableCoinIds() : List<String>
    suspend fun getCoinsWithPrices() : List<Coin>
}