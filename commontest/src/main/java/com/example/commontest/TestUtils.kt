package com.example.commontest

import com.example.domain.models.Coin

object TestUtils {

    fun createFakeCoin(str: String) = Coin(
        id = str,
        imageUrl = "imageUrl",
        symbol = "symbol",
        fullName = "fullName",
        cryptoComparePrice = 15.5,
        coinCapPrice = 9.5,
        observable = true
    )

}