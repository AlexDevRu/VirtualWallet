package com.example.data.data_sources

import com.example.data.dto.CoinListResponseDto
import com.example.data.dto.PriceDto
import com.example.data.services.CryptoCompareService
import com.example.domain.models.Coin
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CryptoCompareDataSourceImplTest {

    private val cryptoCompareService = mockk<CryptoCompareService>()
    private val cryptoCompareDataSourceImpl = CryptoCompareDataSourceImpl(cryptoCompareService)

    @Test
    fun `method getAllCoins() calls getAllCoins() method in api service`() = runBlocking {
        coEvery { cryptoCompareService.getAllCoins() } returns CoinListResponseDto(emptyMap())

        cryptoCompareDataSourceImpl.getAllCoins()

        coVerify(exactly = 1) { cryptoCompareService.getAllCoins() }
    }

    @Test
    fun `method getAllCoins() returns the same data as api service`() = runBlocking {
        val map = mapOf(
            "A" to CoinListResponseDto.CoinDto("1", null, "A", "A"),
            "B" to CoinListResponseDto.CoinDto("2", null, "B", "B"),
            "C" to CoinListResponseDto.CoinDto("3", null, "C", "C"),
        )
        val expected = listOf(
            Coin("1", null, "A", "A", -1.0, -1.0, false),
            Coin("2", null, "B", "B", -1.0, -1.0, false),
            Coin("3", null, "C", "C", -1.0, -1.0, false),
        )

        coEvery { cryptoCompareService.getAllCoins() } returns CoinListResponseDto(map)

        val actual = cryptoCompareDataSourceImpl.getAllCoins()

        assertEquals(expected, actual)
    }

    @Test
    fun `method getPriceInUsd() calls getPrice() method in api service`() = runBlocking {
        coEvery { cryptoCompareService.getPrice(any()) } returns PriceDto(0.0)

        cryptoCompareDataSourceImpl.getPriceInUsd("")

        coVerify(exactly = 1) { cryptoCompareService.getPrice(any()) }
    }

    @Test
    fun `method getPriceInUsd() calls getPrice() method in api service with the same symbol`() = runBlocking {
        coEvery { cryptoCompareService.getPrice(any()) } returns PriceDto(0.0)

        val symbol = "A"
        cryptoCompareDataSourceImpl.getPriceInUsd(symbol)

        coVerify(exactly = 1) { cryptoCompareService.getPrice(symbol) }
    }

    @Test
    fun `method getPriceInUsd() returns USD value in DTO`() = runBlocking {
        val expected = 12.54

        coEvery { cryptoCompareService.getPrice(any()) } returns PriceDto(expected)

        val symbol = "A"
        val actual = cryptoCompareDataSourceImpl.getPriceInUsd(symbol)

        assertEquals(expected, actual, 0.01)
    }

}