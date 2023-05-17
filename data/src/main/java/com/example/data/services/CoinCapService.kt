package com.example.data.services

import com.example.data.dto.CoinCapAssetsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinCapService {
    @GET("assets")
    suspend fun getAssetBySymbol(@Query("search") symbol: String) : CoinCapAssetsDto
}