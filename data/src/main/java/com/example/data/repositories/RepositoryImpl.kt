package com.example.data.repositories

import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.data_sources.LocalDataSource
import com.example.domain.models.Coin
import com.example.domain.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cryptoCompareDataSource: CryptoCompareDataSource,
    private val localDataSource: LocalDataSource
): Repository {

    override suspend fun getAllCoins() = withContext(Dispatchers.IO) {
        try {
            val coins = cryptoCompareDataSource.getAllCoins()
            val observableCoinIds = localDataSource.getObservableCoinIds()
            val coinsWithPrices = localDataSource.getCoinsWithPrices()
            val newCoins = coins.map { coin ->
                coin.copy(
                    price = coinsWithPrices.firstOrNull { it.id == coin.id }?.price ?: -1.0,
                    observable = observableCoinIds.contains(coin.id)
                )
            }
            localDataSource.insertCoins(newCoins)
            localDataSource.getAllCoins()
        } catch (e: Exception) {
            localDataSource.getAllCoins()
        }
    }

    override fun getAllCoinsFlow(query: String): Flow<List<Coin>> {
        return localDataSource.getAllCoinsFlow(query)
    }

    override suspend fun insertCoins(coins: List<Coin>) = withContext(Dispatchers.IO) {
        localDataSource.insertCoins(coins)
    }

    override suspend fun getCoinById(id: String) = withContext(Dispatchers.IO) {
        localDataSource.getCoinById(id)
    }

    override fun getCoinByIdFlow(id: String): Flow<Coin?> {
        return localDataSource.getCoinByIdFlow(id)
    }

    override suspend fun changeObservableCoin(id: String, observable: Boolean) = withContext(Dispatchers.IO) {
        if (observable) {
            try {
                val coin = localDataSource.getCoinById(id) ?: return@withContext
                val priceInUsd = cryptoCompareDataSource.getPriceInUsd(coin.symbol)
                localDataSource.updatePrice(coin.id, priceInUsd)
            } catch (e: Exception) {}
        }
        localDataSource.changeObservableCoin(id, observable)
    }

    override fun getObservableCoinsFlow(): Flow<List<Coin>> {
        return localDataSource.getObservableCoinsFlow()
    }

    override suspend fun updatePrices(coinId: String) = withContext(Dispatchers.IO) {
        try {
            val coin = localDataSource.getCoinById(coinId) ?: return@withContext
            val priceInUsd = cryptoCompareDataSource.getPriceInUsd(coin.symbol)
            localDataSource.updatePrice(coinId, priceInUsd)
        } catch (e: Exception) {}
    }

}