package com.example.data.services

import com.example.data.dto.CoinListResponseDto
import com.example.data.dto.PriceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCompareService {
    @GET("all/coinlist")
    suspend fun getAllCoins(@Query("summary") summary: Boolean = true) : CoinListResponseDto

    @GET("price")
    suspend fun getPrice(@Query("fsym") from: String, @Query("tsyms") to: String = "USD") : PriceDto
}