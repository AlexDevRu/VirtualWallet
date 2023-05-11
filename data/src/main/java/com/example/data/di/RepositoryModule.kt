package com.example.data.di

import com.example.data.repositories.RepositoryImpl
import com.example.domain.repositories.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsRepository(repositoryImpl: RepositoryImpl) : Repository
}