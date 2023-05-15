package com.example.data.data_sources

import android.util.Log
import com.example.data.services.CoinCapService
import com.example.domain.data_sources.CoinCapDataSource
import javax.inject.Inject

class CoinCapDataSourceImpl @Inject constructor(
    private val coinCapService: CoinCapService
) : CoinCapDataSource {

    override suspend fun getAssets(): Map<String, Double> {
        val map = hashMapOf<String, Double>()
        val assets = coinCapService.getAssets().data
        assets.forEach {
            map[it.symbol] = it.priceUsd.toDouble()
        }
        return map
    }

    override suspend fun getPriceInUsd(symbol: String): Double {
        val assets = coinCapService.getAssetBySymbol(symbol).data
        return assets.firstOrNull()?.priceUsd?.toDouble() ?: -1.0
    }
}