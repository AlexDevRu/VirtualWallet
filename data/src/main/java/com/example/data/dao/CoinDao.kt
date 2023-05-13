package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entities.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("select * from coins order by fullName asc")
    suspend fun getAllCoins() : List<CoinEntity>

    @Query("select * from coins where fullName like '%' || :query || '%' order by fullName asc")
    fun getAllCoinsFlow(query: String) : Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("select * from coins where id = :id")
    suspend fun getCoinById(id: String): CoinEntity?

    @Query("select * from coins where id = :id")
    fun getCoinByIdFlow(id: String): Flow<CoinEntity?>

    @Query("update coins set observable = :observable where id = :id")
    suspend fun updateCoinObservable(id: String, observable: Boolean)

    @Query("select id from coins where observable = 1")
    suspend fun getObservableCoinIds() : List<String>

    @Query("select * from coins where price > 0")
    suspend fun getCoinsWithPrices() : List<CoinEntity>

    @Query("select * from coins where observable = 1")
    fun getObservableCoins() : Flow<List<CoinEntity>>

    @Query("update coins set price = :price where id = :id")
    suspend fun updatePrice(id: String, price: Double)
}