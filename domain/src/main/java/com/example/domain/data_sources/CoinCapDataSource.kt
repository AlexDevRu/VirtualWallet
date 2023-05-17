package com.example.domain.data_sources

interface CoinCapDataSource {
    suspend fun getPriceInUsd(symbol: String) : Double
}