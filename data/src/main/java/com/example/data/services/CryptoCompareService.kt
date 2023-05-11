package com.example.data.services

import com.example.data.dto.CoinListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCompareService {

    @GET("all/coinlist")
    suspend fun getAllCoins(@Query("summary") summary: Boolean = true) : CoinListResponseDto

}