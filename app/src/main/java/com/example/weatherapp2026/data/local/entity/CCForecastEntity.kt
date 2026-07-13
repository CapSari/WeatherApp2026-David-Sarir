package com.example.weatherapp2026.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class CCForecastEntity(
    @PrimaryKey val m_date: String,
    val m_dayName: String,
    val m_minTemp: Double,
    val m_maxTemp: Double,
    val m_weatherCondition: String,
    val m_iconCode: String,
    val m_conditionId: Int
)
