package com.example.data.data_sources

import com.example.data.dto.CoinCapAssetsDto
import com.example.data.services.CoinCapService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CoinCapDataSourceImplTest {

    private val coinCapService = mockk<CoinCapService>()
    private val coinCapDataSourceImpl = CoinCapDataSourceImpl(coinCapService)

    @Test
    fun `method getPriceInUsd() calls assets method in api service`() = runBlocking {
        coEvery { coinCapService.getAssetBySymbol(any()) } returns CoinCapAssetsDto(emptyList())

        coinCapDataSourceImpl.getPriceInUsd("A")

        coVerify(exactly = 1) { coinCapService.getAssetBySymbol(any()) }
    }

    @Test
    fun `method getPriceInUsd() calls assets method in api service with the same symbol`() = runBlocking {
        coEvery { coinCapService.getAssetBySymbol(any()) } returns CoinCapAssetsDto(emptyList())

        val symbol = "A"
        coinCapDataSourceImpl.getPriceInUsd(symbol)

        coVerify(exactly = 1) { coinCapService.getAssetBySymbol(symbol) }
    }

    @Test
    fun `method getPriceInUsd() returns the first price from api`() = runBlocking {
        val list = listOf(
            CoinCapAssetsDto.Coin("A", "15.0"),
            CoinCapAssetsDto.Coin("B", "2.0"),
            CoinCapAssetsDto.Coin("C", "4.0"),
        )
        coEvery { coinCapService.getAssetBySymbol(any()) } returns CoinCapAssetsDto(list)

        val expected = 15.0
        val actual = coinCapDataSourceImpl.getPriceInUsd("A")

        assertEquals(expected, actual, 0.01)
    }

}