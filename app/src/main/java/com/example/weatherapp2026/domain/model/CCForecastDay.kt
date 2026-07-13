package com.example.weatherapp2026.domain.model

data class CCForecastDay(
    val m_date: String,
    val m_dayName: String,
    val m_minTemp: Double,
    val m_maxTemp: Double,
    val m_weatherCondition: String,
    val m_iconCode: String,
    val m_conditionId: Int,
    val m_weatherType: CCWeatherType
)
