package com.example.data.repositories

import com.example.commontest.TestUtils
import com.example.domain.data_sources.CoinCapDataSource
import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.data_sources.LocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryImplTest {

    private val cryptoCompareDataSource = mockk<CryptoCompareDataSource>()
    private val coinCapDataSource = mockk<CoinCapDataSource>()
    private val localDataSource = mockk<LocalDataSource>()
    private val repository = spyk(RepositoryImpl(cryptoCompareDataSource, coinCapDataSource, localDataSource))

    @Test
    fun `method getAllCoins() merges local coins and coins from server`() = runBlocking {
        val serverCoins = listOf(
            TestUtils.createFakeCoin("1").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("5").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            )
        )

        val localCoins = listOf(
            TestUtils.createFakeCoin("1").copy(
                cryptoComparePrice = 23.0,
                coinCapPrice = 56.0,
                observable = true
            ),
            TestUtils.createFakeCoin("2").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("3").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("4").copy(
                cryptoComparePrice = 6.0,
                coinCapPrice = 9.0,
                observable = false
            )
        )

        val expected = listOf(
            TestUtils.createFakeCoin("1").copy(
                cryptoComparePrice = 23.0,
                coinCapPrice = 56.0,
                observable = true
            ),
            TestUtils.createFakeCoin("2").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("3").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("4").copy(
                cryptoComparePrice = 6.0,
                coinCapPrice = 9.0,
                observable = false
            ),
            TestUtils.createFakeCoin("5").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            )
        )

        coEvery { cryptoCompareDataSource.getAllCoins() } returns serverCoins
        coEvery { localDataSource.getObservableCoinIds() } returns localCoins.filter { it.observable }.map { it.id }
        coEvery { localDataSource.getCoinsWithPrices() } returns localCoins.filter { it.coinCapPrice > 0 || it.cryptoComparePrice > 0 }
        coEvery { localDataSource.insertCoins(any()) } returns Unit
        coEvery { localDataSource.getAllCoins() } returns expected

        val actual = repository.getAllCoins()

        assertEquals(expected, actual)
    }

    @Test
    fun `method getAllCoins() returns local coins if server exception happens`() = runBlocking {
        val localCoins = listOf(
            TestUtils.createFakeCoin("1").copy(
                cryptoComparePrice = 23.0,
                coinCapPrice = 56.0,
                observable = true
            ),
            TestUtils.createFakeCoin("2").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("3").copy(
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            TestUtils.createFakeCoin("4").copy(
                cryptoComparePrice = 6.0,
                coinCapPrice = 9.0,
                observable = false
            )
        )

        coEvery { cryptoCompareDataSource.getAllCoins() } answers { throw Exception() }
        coEvery { localDataSource.getObservableCoinIds() } returns localCoins.filter { it.observable }.map { it.id }
        coEvery { localDataSource.getCoinsWithPrices() } returns localCoins.filter { it.coinCapPrice > 0 || it.cryptoComparePrice > 0 }
        coEvery { localDataSource.insertCoins(any()) } returns Unit
        coEvery { localDataSource.getAllCoins() } returns localCoins

        val actual = repository.getAllCoins()

        assertEquals(localCoins, actual)
    }

    // getAllCoinsFlow

    @Test
    fun `method getAllCoinsFlow() calls getAllCoinsFlow() method in data source`() = runBlocking {
        every { localDataSource.getAllCoinsFlow(any()) } returns emptyFlow()

        repository.getAllCoinsFlow("")

        verify(exactly = 1) { localDataSource.getAllCoinsFlow(any()) }
    }

    @Test
    fun `method getAllCoinsFlow() calls getAllCoinsFlow() method in data source with the same parameter`() = runBlocking {
        every { localDataSource.getAllCoinsFlow(any()) } returns emptyFlow()

        val query = "test"
        repository.getAllCoinsFlow(query)

        verify(exactly = 1) { localDataSource.getAllCoinsFlow(query) }
    }

    @Test
    fun `method getAllCoinsFlow() returns the same data as getAllCoinsFlow() method in data source`() = runBlocking {
        val expected = List(10) {
            TestUtils.createFakeCoin(it.toString())
        }
        every { localDataSource.getAllCoinsFlow(any()) } returns flowOf(expected)

        val query = "test"
        val actual = repository.getAllCoinsFlow(query).last()

        assertEquals(expected, actual)
    }

    // insertCoins

    @Test
    fun `method insertCoins() calls insertCoins() method in data source`() = runBlocking {
        coEvery { localDataSource.insertCoins(any()) } returns Unit

        repository.insertCoins(emptyList())

        coVerify(exactly = 1) { localDataSource.insertCoins(any()) }
    }

    @Test
    fun `method insertCoins() calls insertCoins() method in data source with the same parameter`() = runBlocking {
        coEvery { localDataSource.insertCoins(any()) } returns Unit

        val coins = List(5) { TestUtils.createFakeCoin(it.toString()) }
        repository.insertCoins(coins)

        coVerify(exactly = 1) { localDataSource.insertCoins(coins) }
    }

    // getCoinById

    @Test
    fun `method getCoinById() calls getCoinById() method in data source`() = runBlocking {
        coEvery { localDataSource.getCoinById(any()) } returns null

        repository.getCoinById("")

        coVerify(exactly = 1) { localDataSource.getCoinById(any()) }
    }

    @Test
    fun `method getCoinById() calls getCoinById() method in data source with the same parameter`() = runBlocking {
        coEvery { localDataSource.getCoinById(any()) } returns null

        val id = "test"
        repository.getCoinById(id)

        coVerify(exactly = 1) { localDataSource.getCoinById(id) }
    }

    @Test
    fun `method getCoinById() returns the same data as getCoinById() method in data source`() = runBlocking {
        val expected = TestUtils.createFakeCoin("1")

        coEvery { localDataSource.getCoinById(any()) } returns expected

        val actual = repository.getCoinById("")

        assertEquals(expected, actual)
    }

    // getCoinByIdFlow

    @Test
    fun `method getCoinByIdFlow() calls getCoinByIdFlow() method in data source`() = runBlocking {
        every { localDataSource.getCoinByIdFlow(any()) } returns emptyFlow()

        repository.getCoinByIdFlow("")

        verify(exactly = 1) { localDataSource.getCoinByIdFlow(any()) }
    }

    @Test
    fun `method getCoinByIdFlow() calls getCoinByIdFlow() method in data source with the same parameter`() = runBlocking {
        every { localDataSource.getCoinByIdFlow(any()) } returns emptyFlow()

        val id = "test"
        repository.getCoinByIdFlow(id)

        verify(exactly = 1) { localDataSource.getCoinByIdFlow(id) }
    }

    @Test
    fun `method getCoinByIdFlow() returns the same data as getCoinByIdFlow() method in data source`() = runBlocking {
        val expected = TestUtils.createFakeCoin("1")

        every { localDataSource.getCoinByIdFlow(any()) } returns flowOf(expected)

        val actual = repository.getCoinByIdFlow("").last()

        assertEquals(expected, actual)
    }

    // changeObservableCoin

    @Test
    fun `method changeObservableCoin() calls changeObservableCoin() method in data source`() = runBlocking {
        coEvery { localDataSource.changeObservableCoin(any(), any()) } returns Unit
        coEvery { repository.updatePrices(any()) } returns Unit

        repository.changeObservableCoin("", true)

        coVerify(exactly = 1) { localDataSource.changeObservableCoin(any(), any()) }
    }

    @Test
    fun `method changeObservableCoin() calls changeObservableCoin() method in data source with the same parameter`() = runBlocking {
        coEvery { localDataSource.changeObservableCoin(any(), any()) } returns Unit
        coEvery { repository.updatePrices(any()) } returns Unit

        val id = "1"
        val observable = true
        repository.changeObservableCoin(id, observable)

        coVerify(exactly = 1) { localDataSource.changeObservableCoin(id, observable) }
    }

    @Test
    fun `method changeObservableCoin() calls updatePrices() if observable is true`() = runBlocking {
        coEvery { localDataSource.changeObservableCoin(any(), any()) } returns Unit
        coEvery { repository.updatePrices(any()) } returns Unit

        val id = "1"
        val observable = true
        repository.changeObservableCoin(id, observable)

        coVerify(exactly = 1) { repository.updatePrices(id) }
    }

    @Test
    fun `method changeObservableCoin() doesn't call updatePrices() if observable is false`() = runBlocking {
        coEvery { localDataSource.changeObservableCoin(any(), any()) } returns Unit
        coEvery { repository.updatePrices(any()) } returns Unit

        val id = "1"
        val observable = false
        repository.changeObservableCoin(id, observable)

        coVerify(exactly = 0) { repository.updatePrices(id) }
    }

    // getObservableCoinsFlow

    @Test
    fun `method getObservableCoinsFlow() calls getObservableCoinsFlow() method in data source`() = runBlocking {
        every { localDataSource.getObservableCoinsFlow() } returns emptyFlow()

        repository.getObservableCoinsFlow()

        verify(exactly = 1) { localDataSource.getObservableCoinsFlow() }
    }

    @Test
    fun `method getObservableCoinsFlow() returns the same data as getObservableCoinsFlow() method in data source`() = runBlocking {
        val expected = List(10) { TestUtils.createFakeCoin(it.toString()) }
        every { localDataSource.getObservableCoinsFlow() } returns flowOf(expected)

        val actual = repository.getObservableCoinsFlow().last()

        assertEquals(expected, actual)
    }

    // getObservableCoins

    @Test
    fun `method getObservableCoins() calls getObservableCoins() method in data source`() = runBlocking {
        coEvery { localDataSource.getObservableCoins() } returns emptyList()

        repository.getObservableCoins()

        coVerify(exactly = 1) { localDataSource.getObservableCoins() }
    }

    @Test
    fun `method getObservableCoins() returns the same data as getObservableCoins() method in data source`() = runBlocking {
        val expected = List(10) { TestUtils.createFakeCoin(it.toString()) }
        coEvery { localDataSource.getObservableCoins() } returns expected

        val actual = repository.getObservableCoins()

        assertEquals(expected, actual)
    }

    // updatePrices

    @Test
    fun `method updatePrices() fetches prices with correct coin symbol`() = runBlocking {
        val priceInUsd1 = 12.0
        val priceInUsd2 = 16.0
        val coin = TestUtils.createFakeCoin("1")

        coEvery { localDataSource.getCoinById(any()) } returns coin
        coEvery { cryptoCompareDataSource.getPriceInUsd(any()) } returns priceInUsd1
        coEvery { coinCapDataSource.getPriceInUsd(any()) } returns priceInUsd2

        val coinId = "1"
        repository.updatePrices(coinId)

        coVerify(exactly = 1) { cryptoCompareDataSource.getPriceInUsd(coin.symbol) }
        coVerify(exactly = 1) { coinCapDataSource.getPriceInUsd(coin.symbol) }
    }

    @Test
    fun `method updatePrices() calls updatePrice() method in data source with prices from api`() = runBlocking {
        val priceInUsd1 = 12.0
        val priceInUsd2 = 16.0
        val coin = TestUtils.createFakeCoin("1")

        coEvery { localDataSource.getCoinById(any()) } returns coin
        coEvery { cryptoCompareDataSource.getPriceInUsd(any()) } returns priceInUsd1
        coEvery { coinCapDataSource.getPriceInUsd(any()) } returns priceInUsd2

        val coinId = "1"
        repository.updatePrices(coinId)

        coVerify(exactly = 1) { localDataSource.updatePrice(coinId, priceInUsd1, priceInUsd2) }
    }

    @Test
    fun `method updatePrices() doesn't call updatePrice() method in data source if api throws exception`() = runBlocking {
        val priceInUsd1 = 12.0
        val priceInUsd2 = 16.0
        val coin = TestUtils.createFakeCoin("1")

        coEvery { localDataSource.getCoinById(any()) } returns coin
        coEvery { cryptoCompareDataSource.getPriceInUsd(any()) } answers { throw Exception() }
        coEvery { coinCapDataSource.getPriceInUsd(any()) } returns priceInUsd2

        val coinId = "1"
        repository.updatePrices(coinId)

        coVerify(exactly = 0) { localDataSource.updatePrice(coinId, priceInUsd1, priceInUsd2) }
    }
}