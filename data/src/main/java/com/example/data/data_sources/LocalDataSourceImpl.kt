package com.example.data.data_sources

import com.example.data.dao.CoinDao
import com.example.data.mappers.toDomainModel
import com.example.data.mappers.toEntity
import com.example.domain.data_sources.LocalDataSource
import com.example.domain.models.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val coinDao: CoinDao
) : LocalDataSource {
    override suspend fun insertCoins(coins: List<Coin>) {
        coinDao.insertCoins(coins.map { it.toEntity() })
    }

    override suspend fun getAllCoins(): List<Coin> {
        return coinDao.getAllCoins().map { it.toDomainModel() }
    }

    override fun getAllCoinsFlow(query: String): Flow<List<Coin>> {
        return coinDao.getAllCoinsFlow(query.trim()).map { it.map { it.toDomainModel() } }
    }

    override suspend fun getCoinById(id: String): Coin? {
        return coinDao.getCoinById(id)?.toDomainModel()
    }

    override fun getCoinByIdFlow(id: String): Flow<Coin?> {
        return coinDao.getCoinByIdFlow(id).map { it?.toDomainModel() }
    }

    override suspend fun changeObservableCoin(id: String, observable: Boolean) {
        coinDao.updateCoinObservable(id, observable)
    }

    override fun getObservableCoinsFlow(): Flow<List<Coin>> {
        return coinDao.getObservableCoins().map { it.map { it.toDomainModel() } }
    }

    override suspend fun updatePrice(id: String, price: Double) {
        coinDao.updatePrice(id, price)
    }

    override suspend fun getObservableCoinIds(): List<String> {
        return coinDao.getObservableCoinIds()
    }

    override suspend fun getCoinsWithPrices(): List<Coin> {
        return coinDao.getCoinsWithPrices().map { it.toDomainModel() }
    }
}