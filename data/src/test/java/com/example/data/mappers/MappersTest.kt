package com.example.data.mappers

import com.example.data.dto.CoinListResponseDto
import com.example.data.entities.CoinEntity
import com.example.domain.models.Coin
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun `test mapping from dto to domain model`() {
        val coinListResponseDto  = CoinListResponseDto(
            data = mapOf(
                "1" to CoinListResponseDto.CoinDto("1", "1", "1", "1"),
                "2" to CoinListResponseDto.CoinDto("2", "2", "2", "2"),
                "3" to CoinListResponseDto.CoinDto("3", "3", "3", "3"),
            )
        )

        val expected = listOf(
            Coin(
                id = "1",
                symbol = "1",
                fullName = "1",
                imageUrl = "1",
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            Coin(
                id = "2",
                symbol = "2",
                fullName = "2",
                imageUrl = "2",
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
            Coin(
                id = "3",
                symbol = "3",
                fullName = "3",
                imageUrl = "3",
                cryptoComparePrice = -1.0,
                coinCapPrice = -1.0,
                observable = false
            ),
        )

        val actual = coinListResponseDto.toDomainModel()

        assertEquals(expected, actual)
    }

    @Test
    fun `test mapping from domain model to entity`() {
        val coin  = Coin(
            id = "1",
            symbol = "2",
            fullName = "3",
            imageUrl = "4",
            cryptoComparePrice = 12.0,
            coinCapPrice = 4.0,
            observable = true
        )

        val expected = CoinEntity(
            id = "1",
            symbol = "2",
            fullName = "3",
            imageUrl = "4",
            cryptoComparePrice = 12.0,
            coinCapPrice = 4.0,
            observable = true
        )

        val actual = coin.toEntity()

        assertEquals(expected, actual)
    }

    @Test
    fun `test mapping from entity to domain model`() {
        val coinEntity = CoinEntity(
            id = "1",
            symbol = "2",
            fullName = "3",
            imageUrl = "4",
            cryptoComparePrice = 12.0,
            coinCapPrice = 4.0,
            observable = true
        )

        val expected = Coin(
            id = "1",
            symbol = "2",
            fullName = "3",
            imageUrl = "4",
            cryptoComparePrice = 12.0,
            coinCapPrice = 4.0,
            observable = true
        )

        val actual = coinEntity.toDomainModel()

        assertEquals(expected, actual)
    }

}