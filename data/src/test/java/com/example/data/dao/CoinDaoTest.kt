package com.example.data.dao

import androidx.room.Room
import com.example.data.entities.CoinEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class CoinDaoTest {

    private val appContext = RuntimeEnvironment.getApplication()

    private lateinit var database: AppDatabase
    private lateinit var coinDao: CoinDao

    @Before
    fun setUp() {
        database = Room
            .inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        coinDao = database.getCoinsDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `insert coins`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, true)
        }
        coinDao.insertCoins(list)
        val allCoins = coinDao.getAllCoins()
        assertTrue(allCoins.containsAll(list))
    }

    @Test
    fun `get coin by id`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, true)
        }
        coinDao.insertCoins(list)
        val coin = coinDao.getCoinById("1")
        assertTrue(coin != null)
    }

    @Test
    fun `get coin by id flow`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, true)
        }
        coinDao.insertCoins(list)
        val coin = coinDao.getCoinByIdFlow("1").firstOrNull()
        assertTrue(coin != null)
    }

    @Test
    fun `update coin observable`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, false)
        }
        coinDao.insertCoins(list)
        coinDao.updateCoinObservable("1", true)
        val coin = coinDao.getCoinById("1")
        assertTrue(coin?.observable == true)
    }

    @Test
    fun `get observable coin ids`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, it % 2 == 0)
        }
        coinDao.insertCoins(list)
        val expected = listOf("0", "2", "4", "6", "8")
        val actual = coinDao.getObservableCoinIds()
        assertEquals(expected, actual)
    }

    @Test
    fun `get coins with prices`() = runBlocking {
        val list = List(10) {
            val k = if (it % 2 == 0) 1 else 0
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54 * k, 34.67 * k, it % 2 == 0)
        }
        coinDao.insertCoins(list)
        val expected = list.filter { it.coinCapPrice > 0 || it.cryptoComparePrice > 0 }
        val actual = coinDao.getCoinsWithPrices()
        assertEquals(expected, actual)
    }

    @Test
    fun `get observable coins flow`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, it % 2 == 0)
        }
        coinDao.insertCoins(list)
        val expected = listOf(list[0], list[2], list[4], list[6], list[8])
        val actual = coinDao.getObservableCoinsFlow().firstOrNull()
        assertEquals(expected, actual)
    }

    @Test
    fun `get observable coins`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, it % 2 == 0)
        }
        coinDao.insertCoins(list)
        val expected = listOf(list[0], list[2], list[4], list[6], list[8])
        val actual = coinDao.getObservableCoins()
        assertEquals(expected, actual)
    }

    @Test
    fun `update price`() = runBlocking {
        val list = List(10) {
            CoinEntity(it.toString(), "imageUrl", "A", "A", 12.54, 34.67, it % 2 == 0)
        }
        coinDao.insertCoins(list)

        val id = "1"
        val cryptoComparePrice = 12.0
        val coinCapPrice = 1.0
        coinDao.updatePrice(id, cryptoComparePrice, coinCapPrice)

        val entity = coinDao.getCoinById(id)!!

        assertEquals(cryptoComparePrice, entity.cryptoComparePrice, 0.01)
        assertEquals(coinCapPrice, entity.coinCapPrice, 0.01)
    }
}