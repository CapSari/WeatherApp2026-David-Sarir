package com.example.weatherapp2026.util

import com.example.weatherapp2026.BuildConfig

object CCConstants {
    val API_KEY: String get() = BuildConfig.OPEN_WEATHER_API_KEY
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val UNITS_METRIC = "metric"
    const val DEFAULT_LAT = -26.2023
    const val DEFAULT_LON = 28.0436
    const val DB_NAME = "weather_db"
    const val CACHE_EXPIRY_MS = 30 * 60 * 1000L // 30 minutes
}
