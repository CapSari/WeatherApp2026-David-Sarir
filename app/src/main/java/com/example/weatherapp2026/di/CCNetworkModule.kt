package com.example.weatherapp2026.di

import com.example.weatherapp2026.BuildConfig
import com.example.weatherapp2026.data.remote.CCWeatherApiService
import com.example.weatherapp2026.util.CCConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CCNetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val m_loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG_MODE) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(m_loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(m_okHttpClient: OkHttpClient, m_gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(CCConstants.BASE_URL)
            .client(m_okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(m_gson))
            .build()

    @Provides
    @Singleton
    fun provideWeatherApiService(m_retrofit: Retrofit): CCWeatherApiService =
        m_retrofit.create(CCWeatherApiService::class.java)
}
