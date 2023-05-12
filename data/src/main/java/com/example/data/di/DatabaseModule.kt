package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.dao.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "virtual_wallet_db"
    ).build()

    @Singleton
    @Provides
    fun provideCoinDao(db: AppDatabase) = db.getCoinsDao()
}