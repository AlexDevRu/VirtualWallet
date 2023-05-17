package com.example.data.data_sources

import com.example.data.dao.CoinDao
import com.example.data.entities.CoinEntity
import com.example.domain.models.Coin
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalDataSourceImplTest {

    private val coinDao = mockk<CoinDao>()
    private val localDataSourceImpl = LocalDataSourceImpl(coinDao)

    // insertCoins

    @Test
    fun `method insertCoins() calls insertCoins() method in dao`() = runBlocking {
        coEvery { coinDao.insertCoins(any()) } returns Unit

        localDataSourceImpl.insertCoins(emptyList())

        coVerify(exactly = 1) { coinDao.insertCoins(any()) }
    }

    @Test
    fun `method insertCoins() calls insertCoins() method in dao with the same list`() = runBlocking {
        coEvery { coinDao.insertCoins(any()) } returns Unit

        val list1 = listOf(
            Coin("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            Coin("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            Coin("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        val list2 = listOf(
            CoinEntity("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            CoinEntity("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            CoinEntity("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        localDataSourceImpl.insertCoins(list1)

        coVerify(exactly = 1) { coinDao.insertCoins(list2) }
    }

    // getAllCoins

    @Test
    fun `method getAllCoins() calls getAllCoins() method in dao`() = runBlocking {
        coEvery { coinDao.getAllCoins() } returns emptyList()

        localDataSourceImpl.getAllCoins()

        coVerify(exactly = 1) { coinDao.getAllCoins() }
    }

    @Test
    fun `method getAllCoins() returns the same data as getAllCoins() method in dao`() = runBlocking {
        val entities = List(10) {
            CoinEntity(it.toString(), it.toString(), it.toString(), it.toString(), it.toDouble(), it.toDouble(), true)
        }
        val expected = List(10) {
            Coin(it.toString(), it.toString(), it.toString(), it.toString(), it.toDouble(), it.toDouble(), true)
        }
        coEvery { coinDao.getAllCoins() } returns entities

        val actual = localDataSourceImpl.getAllCoins()

        assertEquals(expected, actual)
    }

    // getAllCoinsFlow

    @Test
    fun `method getAllCoinsFlow() calls getAllCoinsFlow() method in dao`() = runBlocking {
        every { coinDao.getAllCoinsFlow(any()) } returns emptyFlow()

        localDataSourceImpl.getAllCoinsFlow("")

        verify(exactly = 1) { coinDao.getAllCoinsFlow(any()) }
    }

    @Test
    fun `method getAllCoinsFlow() calls getAllCoinsFlow() method in dao with the same parameter`() = runBlocking {
        every { coinDao.getAllCoinsFlow(any()) } returns emptyFlow()

        val query = "test"
        localDataSourceImpl.getAllCoinsFlow(query)

        verify(exactly = 1) { coinDao.getAllCoinsFlow(query) }
    }

    @Test
    fun `method getAllCoinsFlow() returns the same data as getAllCoinsFlow() method in dao`() = runBlocking {
        val entities = List(10) {
            CoinEntity(it.toString(), it.toString(), it.toString(), it.toString(), it.toDouble(), it.toDouble(), true)
        }
        val expected = List(10) {
            Coin(it.toString(), it.toString(), it.toString(), it.toString(), it.toDouble(), it.toDouble(), true)
        }
        every { coinDao.getAllCoinsFlow(any()) } returns flowOf(entities)

        val query = "test"
        val actual = localDataSourceImpl.getAllCoinsFlow(query).last()

        assertEquals(expected, actual)
    }

    // getCoinById

    @Test
    fun `method getCoinById() calls getCoinById() method in dao`() = runBlocking {
        coEvery { coinDao.getCoinById(any()) } returns null

        localDataSourceImpl.getCoinById("")

        coVerify(exactly = 1) { coinDao.getCoinById(any()) }
    }

    @Test
    fun `method getCoinById() calls getCoinById() method in dao with the same parameter`() = runBlocking {
        coEvery { coinDao.getCoinById(any()) } returns null

        val id = "test"
        localDataSourceImpl.getCoinById(id)

        coVerify(exactly = 1) { coinDao.getCoinById(id) }
    }

    @Test
    fun `method getCoinById() returns the same data as getCoinById() method in dao`() = runBlocking {
        val coinEntity = CoinEntity("1", "imageUrl", "A", "A", 12.54, 34.67, true)
        val expected = Coin("1", "imageUrl", "A", "A", 12.54, 34.67, true)

        coEvery { coinDao.getCoinById(any()) } returns coinEntity

        val actual = localDataSourceImpl.getCoinById("")

        assertEquals(expected, actual)
    }

    // getCoinByIdFlow

    @Test
    fun `method getCoinByIdFlow() calls getCoinByIdFlow() method in dao`() = runBlocking {
        every { coinDao.getCoinByIdFlow(any()) } returns emptyFlow()

        localDataSourceImpl.getCoinByIdFlow("")

        verify(exactly = 1) { coinDao.getCoinByIdFlow(any()) }
    }

    @Test
    fun `method getCoinByIdFlow() calls getCoinByIdFlow() method in dao with the same parameter`() = runBlocking {
        every { coinDao.getCoinByIdFlow(any()) } returns emptyFlow()

        val id = "test"
        localDataSourceImpl.getCoinByIdFlow(id)

        verify(exactly = 1) { coinDao.getCoinByIdFlow(id) }
    }

    @Test
    fun `method getCoinByIdFlow() returns the same data as getCoinByIdFlow() method in dao`() = runBlocking {
        val coinEntity = CoinEntity("1", "imageUrl", "A", "A", 12.54, 34.67, true)
        val expected = Coin("1", "imageUrl", "A", "A", 12.54, 34.67, true)

        every { coinDao.getCoinByIdFlow(any()) } returns flowOf(coinEntity)

        val actual = localDataSourceImpl.getCoinByIdFlow("").last()

        assertEquals(expected, actual)
    }

    // changeObservableCoin

    @Test
    fun `method changeObservableCoin() calls changeObservableCoin() method in dao`() = runBlocking {
        coEvery { coinDao.updateCoinObservable(any(), any()) } returns Unit

        localDataSourceImpl.changeObservableCoin("", true)

        coVerify(exactly = 1) { coinDao.updateCoinObservable(any(), any()) }
    }

    @Test
    fun `method changeObservableCoin() calls changeObservableCoin() method in dao with the same parameter`() = runBlocking {
        coEvery { coinDao.updateCoinObservable(any(), any()) } returns Unit

        val id = "test"
        val observable = true
        localDataSourceImpl.changeObservableCoin(id, observable)

        coVerify(exactly = 1) { coinDao.updateCoinObservable(id, observable) }
    }

    // getObservableCoinsFlow

    @Test
    fun `method getObservableCoinsFlow() calls getObservableCoinsFlow() method in dao`() = runBlocking {
        every { coinDao.getObservableCoinsFlow() } returns emptyFlow()

        localDataSourceImpl.getObservableCoinsFlow()

        verify(exactly = 1) { coinDao.getObservableCoinsFlow() }
    }

    @Test
    fun `method getObservableCoinsFlow() returns the same data as getObservableCoinsFlow() method in dao`() = runBlocking {
        val expected = listOf(
            Coin("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            Coin("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            Coin("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        val list = listOf(
            CoinEntity("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            CoinEntity("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            CoinEntity("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        every { coinDao.getObservableCoinsFlow() } returns flowOf(list)

        val actual = localDataSourceImpl.getObservableCoinsFlow().last()

        assertEquals(expected, actual)
    }

    // getObservableCoins

    @Test
    fun `method getObservableCoins() calls getObservableCoins() method in dao`() = runBlocking {
        coEvery { coinDao.getObservableCoins() } returns emptyList()

        localDataSourceImpl.getObservableCoins()

        coVerify(exactly = 1) { coinDao.getObservableCoins() }
    }

    @Test
    fun `method getObservableCoins() returns the same data as getObservableCoins() method in dao`() = runBlocking {
        val expected = listOf(
            Coin("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            Coin("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            Coin("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        val list = listOf(
            CoinEntity("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            CoinEntity("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            CoinEntity("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        coEvery { coinDao.getObservableCoins() } returns list

        val actual = localDataSourceImpl.getObservableCoins()

        assertEquals(expected, actual)
    }

    // updatePrice

    @Test
    fun `method updatePrice() calls updatePrice() method in dao`() = runBlocking {
        coEvery { coinDao.updatePrice(any(), any(), any()) } returns Unit

        localDataSourceImpl.updatePrice("", 0.0, 0.0)

        coVerify(exactly = 1) { coinDao.updatePrice(any(), any(), any()) }
    }

    @Test
    fun `method updatePrice() calls updatePrice() method in dao with the same parameter`() = runBlocking {
        coEvery { coinDao.updatePrice(any(), any(), any()) } returns Unit

        val id = "test"
        val cryptoComparePrice = 1.0
        val coinCapPrice = 2.0
        localDataSourceImpl.updatePrice(id, cryptoComparePrice, coinCapPrice)

        coVerify(exactly = 1) { coinDao.updatePrice(id, cryptoComparePrice, coinCapPrice) }
    }

    // getObservableCoinIds

    @Test
    fun `method getObservableCoinIds() calls getObservableCoinIds() method in dao`() = runBlocking {
        coEvery { coinDao.getObservableCoinIds() } returns emptyList()

        localDataSourceImpl.getObservableCoinIds()

        coVerify(exactly = 1) { coinDao.getObservableCoinIds() }
    }

    @Test
    fun `method getObservableCoinIds() returns the same data as getObservableCoinIds() method in dao`() = runBlocking {
        val expected = List(10) { it.toString() }
        coEvery { coinDao.getObservableCoinIds() } returns expected

        val actual = localDataSourceImpl.getObservableCoinIds()

        assertEquals(expected, actual)
    }

    // getCoinsWithPrices

    @Test
    fun `method getCoinsWithPrices() calls getCoinsWithPrices() method in dao`() = runBlocking {
        coEvery { coinDao.getCoinsWithPrices() } returns emptyList()

        localDataSourceImpl.getCoinsWithPrices()

        coVerify(exactly = 1) { coinDao.getCoinsWithPrices() }
    }

    @Test
    fun `method getCoinsWithPrices() returns the same data as getCoinsWithPrices() method in dao`() = runBlocking {
        val expected = listOf(
            Coin("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            Coin("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            Coin("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        val list = listOf(
            CoinEntity("1", "imageUrl", "A", "A", 12.54, 34.67, true),
            CoinEntity("2", "imageUrl2", "B", "B", 11.54, 30.67, false),
            CoinEntity("3", "imageUrl3", "C", "C", 10.54, 3.67, true),
        )
        coEvery { coinDao.getCoinsWithPrices() } returns list

        val actual = localDataSourceImpl.getCoinsWithPrices()

        assertEquals(expected, actual)
    }

}