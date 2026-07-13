package com.example.weatherapp2026.domain.usecase

import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.repository.CCWeatherRepository
import com.example.weatherapp2026.util.CCResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CCGetForecastUseCase @Inject constructor(
    private val m_repository: CCWeatherRepository
) {
    operator fun invoke(m_lat: Double, m_lon: Double): Flow<CCResult<List<CCForecastDay>>> =
        m_repository.getFiveDayForecast(m_lat, m_lon)
}
