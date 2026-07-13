package com.example.weatherapp2026.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp2026.data.local.CCWeatherDatabase
import com.example.weatherapp2026.data.local.dao.CCWeatherDao
import com.example.weatherapp2026.util.CCConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CCDatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext m_context: Context): CCWeatherDatabase =
        Room.databaseBuilder(
            m_context,
            CCWeatherDatabase::class.java,
            CCConstants.DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideWeatherDao(m_database: CCWeatherDatabase): CCWeatherDao =
        m_database.weatherDao()
}
