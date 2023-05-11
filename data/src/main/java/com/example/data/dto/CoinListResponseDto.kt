package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class CoinListResponseDto(
    @SerializedName("Data")
    val data: Map<String, CoinDto>
) {
    data class CoinDto(
        @SerializedName("Id")
        val id: String,
        @SerializedName("ImageUrl")
        val imageUrl: String,
        @SerializedName("Symbol")
        val symbol: String,
        @SerializedName("FullName")
        val fullName: String
    )
}
