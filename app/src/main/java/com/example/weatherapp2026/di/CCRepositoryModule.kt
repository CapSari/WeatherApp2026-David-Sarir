package com.example.weatherapp2026.di

import com.example.weatherapp2026.data.repository.CCWeatherRepositoryImpl
import com.example.weatherapp2026.domain.repository.CCWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CCRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        m_impl: CCWeatherRepositoryImpl
    ): CCWeatherRepository
}
