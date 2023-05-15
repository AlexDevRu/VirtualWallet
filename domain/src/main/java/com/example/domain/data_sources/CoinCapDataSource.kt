package com.example.domain.data_sources

interface CoinCapDataSource {
    suspend fun getAssets() : Map<String, Double>
    suspend fun getPriceInUsd(symbol: String) : Double
}