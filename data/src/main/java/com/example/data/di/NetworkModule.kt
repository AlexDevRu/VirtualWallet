package com.example.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.data.BuildConfig
import com.example.data.services.CryptoCompareService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideCryptoCompareService(
        @ApplicationContext app: Context
    ) : CryptoCompareService {
        val headerInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest
                .newBuilder()
                .header("authorization", "Apikey ${BuildConfig.CRYPTO_COMPARE_KEY}")
                .build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(ChuckerInterceptor.Builder(app).build())
            .addInterceptor(headerInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.CRYPTO_COMPARE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .build()

        return retrofit.create(CryptoCompareService::class.java)
    }

}