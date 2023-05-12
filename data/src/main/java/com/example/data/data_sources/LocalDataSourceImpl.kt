package com.example.data.data_sources

import com.example.data.dao.CoinDao
import com.example.data.mappers.toDomainModel
import com.example.data.mappers.toEntity
import com.example.domain.data_sources.LocalDataSource
import com.example.domain.models.Coin
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

    override suspend fun getCoinById(id: String): Coin? {
        return coinDao.getCoinById(id)?.toDomainModel()
    }
}