package com.example.data.dto

data class CoinCapAssetsDto(
    val data: List<Coin>
) {
    data class Coin(
        val symbol: String,
        val priceUsd: String
    )
}
