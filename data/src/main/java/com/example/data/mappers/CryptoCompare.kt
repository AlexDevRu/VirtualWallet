package com.example.data.mappers

import com.example.data.dto.CoinListResponseDto
import com.example.data.entities.CoinEntity
import com.example.domain.models.Coin

fun CoinListResponseDto.toDomainModel() = data.map { (_, coin) ->
    Coin(
        id = coin.id,
        symbol = coin.symbol,
        fullName = coin.fullName,
        imageUrl = coin.imageUrl,
        price = -1.0
    )
}

fun Coin.toEntity() = CoinEntity(
    id = id,
    symbol = symbol,
    fullName = fullName,
    imageUrl = imageUrl,
    price = price
)

fun CoinEntity.toDomainModel() = Coin(
    id = id,
    symbol = symbol,
    fullName = fullName,
    imageUrl = imageUrl,
    price = price
)