package com.example.weatherapp2026.domain.model

enum class CCWeatherType {
    SUNNY, CLOUDY, RAINY;

    companion object {
        fun from(m_conditionId: Int): CCWeatherType = when (m_conditionId) {
            in 200..299 -> RAINY   // Thunderstorm
            in 300..399 -> RAINY   // Drizzle
            in 500..599 -> RAINY   // Rain
            in 600..699 -> CLOUDY  // Snow
            in 700..799 -> CLOUDY  // Atmosphere (mist, fog, etc.)
            800 -> SUNNY           // Clear sky
            in 801..899 -> CLOUDY  // Clouds
            else -> SUNNY
        }
    }
}
