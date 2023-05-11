package com.example.domain.repositories

import com.example.domain.models.Coin

interface Repository {
    suspend fun getAllCoins() : List<Coin>
}