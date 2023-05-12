package com.example.data.di

import com.example.data.data_sources.CryptoCompareDataSourceImpl
import com.example.data.data_sources.LocalDataSourceImpl
import com.example.domain.data_sources.CryptoCompareDataSource
import com.example.domain.data_sources.LocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {
    @Singleton
    @Binds
    abstract fun bindsCryptoCompareDataSource(cryptoCompareDataSourceImpl: CryptoCompareDataSourceImpl) : CryptoCompareDataSource

    @Singleton
    @Binds
    abstract fun bindsLocalDataSource(localDataSourceImpl: LocalDataSourceImpl) : LocalDataSource
}