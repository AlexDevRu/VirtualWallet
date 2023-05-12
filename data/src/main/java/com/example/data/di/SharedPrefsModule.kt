package com.example.data.di

import com.example.data.utils.SharedPrefsImpl
import com.example.domain.utils.SharedPrefs
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPrefsModule {
    @Singleton
    @Binds
    abstract fun bindsSharedPrefs(sharedPrefsImpl: SharedPrefsImpl) : SharedPrefs
}