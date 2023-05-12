package com.example.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val imageUrl: String?,
    val symbol: String,
    val fullName: String,
    val price: Double,
    val observable: Boolean = false
)
