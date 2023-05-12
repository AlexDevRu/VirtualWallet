package com.example.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.CoinEntity

@Database(
    entities = [CoinEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCoinsDao(): CoinDao
}