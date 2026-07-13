package com.example.weatherapp2026.domain.repository

import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.util.CCResult
import kotlinx.coroutines.flow.Flow

interface CCWeatherRepository {
    fun getCurrentWeather(m_lat: Double, m_lon: Double): Flow<CCResult<CCCurrentWeather>>
    fun getFiveDayForecast(m_lat: Double, m_lon: Double): Flow<CCResult<List<CCForecastDay>>>
}
