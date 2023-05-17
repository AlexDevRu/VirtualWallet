package com.example.data.mappers

import com.example.data.dto.CoinListResponseDto
import com.example.data.entities.CoinEntity
import com.example.domain.models.Coin

interface Mapper<E, D> {
    fun toDomainModel(domainModel : E) : D
    fun toEntity(entity : D) : E
}

fun CoinListResponseDto.toDomainModel() = data.map { (_, coin) ->
    Coin(
        id = coin.id,
        symbol = coin.symbol,
        fullName = coin.fullName,
        imageUrl = coin.imageUrl,
        cryptoComparePrice = -1.0,
        coinCapPrice = -1.0,
        observable = false
    )
}

fun Coin.toEntity() = CoinEntity(
    id = id,
    symbol = symbol,
    fullName = fullName,
    imageUrl = imageUrl,
    cryptoComparePrice = cryptoComparePrice,
    coinCapPrice = coinCapPrice,
    observable = observable
)

fun CoinEntity.toDomainModel() = Coin(
    id = id,
    symbol = symbol,
    fullName = fullName,
    imageUrl = imageUrl,
    cryptoComparePrice = cryptoComparePrice,
    coinCapPrice = coinCapPrice,
    observable = observable
)