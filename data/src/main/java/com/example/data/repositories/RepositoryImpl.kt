package com.example.data.repositories

import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.data_sources.LocalDataSource
import com.example.domain.models.Coin
import com.example.domain.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cryptoCompareDataSource: CryptoCompareDataSource,
    private val localDataSource: LocalDataSource
): Repository {

    override suspend fun getAllCoins() = withContext(Dispatchers.IO) {
        try {
            val coins = cryptoCompareDataSource.getAllCoins()
            localDataSource.insertCoins(coins)
            localDataSource.getAllCoins()
        } catch (e: Exception) {
            localDataSource.getAllCoins()
        }
    }

    override suspend fun insertCoins(coins: List<Coin>) = withContext(Dispatchers.IO) {
        localDataSource.insertCoins(coins)
    }

    override suspend fun getCoinById(id: String) = withContext(Dispatchers.IO) {
        localDataSource.getCoinById(id)
    }

    override suspend fun changeObservableCoin(id: String, observable: Boolean) = withContext(Dispatchers.IO) {
        localDataSource.changeObservableCoin(id, observable)
    }

    override fun getObservableCoinsFlow(): Flow<List<Coin>> {
        return localDataSource.getObservableCoinsFlow()
    }

}