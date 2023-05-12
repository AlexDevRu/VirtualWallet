package com.example.domain.models

data class Coin(
    val id: String,
    val imageUrl: String?,
    val symbol: String,
    val fullName: String,
    val price: Double
) {
    fun getFullImageUrl() = "https://www.cryptocompare.com$imageUrl"
}
