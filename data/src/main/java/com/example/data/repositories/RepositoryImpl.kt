package com.example.data.repositories

import com.example.domain.data_sources.CoinCapDataSource
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
    private val coinCapDataSource: CoinCapDataSource,
    private val localDataSource: LocalDataSource
): Repository {

    override suspend fun getAllCoins() = withContext(Dispatchers.IO) {
        try {
            val coins = cryptoCompareDataSource.getAllCoins()
            val observableCoinIds = localDataSource.getObservableCoinIds()
            val coinsWithPrices = localDataSource.getCoinsWithPrices()
            val newCoins = coins.map { coin ->
                val localCoin = coinsWithPrices.firstOrNull { it.id == coin.id }
                coin.copy(
                    cryptoComparePrice = localCoin?.cryptoComparePrice ?: -1.0,
                    coinCapPrice = localCoin?.coinCapPrice ?: -1.0,
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
            updatePrices(id)
        }
        localDataSource.changeObservableCoin(id, observable)
    }

    override fun getObservableCoinsFlow(): Flow<List<Coin>> {
        return localDataSource.getObservableCoinsFlow()
    }

    override suspend fun getObservableCoins() = withContext(Dispatchers.IO) {
        localDataSource.getObservableCoins()
    }

    override suspend fun updatePrices(coinId: String) = withContext(Dispatchers.IO) {
        try {
            val coin = localDataSource.getCoinById(coinId) ?: return@withContext
            val priceInUsd1 = cryptoCompareDataSource.getPriceInUsd(coin.symbol)
            val priceInUsd2 = coinCapDataSource.getPriceInUsd(coin.symbol)
            localDataSource.updatePrice(coinId, priceInUsd1, priceInUsd2)
        } catch (e: Exception) {}
    }

}