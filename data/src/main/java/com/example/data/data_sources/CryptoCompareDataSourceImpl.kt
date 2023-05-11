package com.example.data.data_sources

import com.example.data.mappers.toDomainModel
import com.example.data.services.CryptoCompareService
import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.models.Coin
import javax.inject.Inject

class CryptoCompareDataSourceImpl @Inject constructor(
    private val cryptoCompareService: CryptoCompareService
): CryptoCompareDataSource {

    override suspend fun getAllCoins(): List<Coin> {
        return cryptoCompareService.getAllCoins().toDomainModel()
    }

}