package com.example.weatherapp2026.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CCCurrentWeatherEntity(
    @PrimaryKey val m_id: Int = 1,
    val m_cityName: String,
    val m_temperature: Double,
    val m_feelsLike: Double,
    val m_humidity: Int,
    val m_weatherCondition: String,
    val m_weatherDescription: String,
    val m_iconCode: String,
    val m_windSpeed: Double,
    val m_conditionId: Int,
    val m_timestamp: Long
)
