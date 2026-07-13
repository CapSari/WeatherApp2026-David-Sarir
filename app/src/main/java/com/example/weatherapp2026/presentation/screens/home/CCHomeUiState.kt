package com.example.weatherapp2026.presentation.screens.home

import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType

sealed class CCHomeUiState {
    data object Loading : CCHomeUiState()

    data class Success(
        val m_currentWeather: CCCurrentWeather,
        val m_forecast: List<CCForecastDay>,
        val m_weatherType: CCWeatherType,
        val m_isFromCache: Boolean = false
    ) : CCHomeUiState()

    data class Error(val m_message: String) : CCHomeUiState()
}
