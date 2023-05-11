package com.example.data.repositories

import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cryptoCompareDataSource: CryptoCompareDataSource
): Repository {

    override suspend fun getAllCoins() = withContext(Dispatchers.IO) {
        try {
            cryptoCompareDataSource.getAllCoins()
        } catch (e: HttpException) {
            emptyList()
        }
    }

}