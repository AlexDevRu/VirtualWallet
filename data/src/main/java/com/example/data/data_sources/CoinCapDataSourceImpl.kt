package com.example.data.data_sources

import com.example.data.services.CoinCapService
import com.example.domain.data_sources.CoinCapDataSource
import javax.inject.Inject

class CoinCapDataSourceImpl @Inject constructor(
    private val coinCapService: CoinCapService
) : CoinCapDataSource {

    override suspend fun getPriceInUsd(symbol: String): Double {
        val assets = coinCapService.getAssetBySymbol(symbol).data
        return assets.firstOrNull()?.priceUsd?.toDouble() ?: -1.0
    }
}