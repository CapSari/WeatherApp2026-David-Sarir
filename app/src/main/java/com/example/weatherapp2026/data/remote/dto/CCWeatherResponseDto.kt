package com.example.weatherapp2026.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CCWeatherResponseDto(
    @SerializedName("coord") val m_coord: CCCoordDto,
    @SerializedName("weather") val m_weather: List<CCWeatherConditionDto>,
    @SerializedName("main") val m_main: CCMainDto,
    @SerializedName("wind") val m_wind: CCWindDto,
    @SerializedName("name") val m_name: String,
    @SerializedName("dt") val m_dt: Long
)

data class CCCoordDto(
    @SerializedName("lat") val m_lat: Double,
    @SerializedName("lon") val m_lon: Double
)

data class CCWeatherConditionDto(
    @SerializedName("id") val m_id: Int,
    @SerializedName("main") val m_main: String,
    @SerializedName("description") val m_description: String,
    @SerializedName("icon") val m_icon: String
)

data class CCMainDto(
    @SerializedName("temp") val m_temp: Double,
    @SerializedName("feels_like") val m_feelsLike: Double,
    @SerializedName("temp_min") val m_tempMin: Double,
    @SerializedName("temp_max") val m_tempMax: Double,
    @SerializedName("humidity") val m_humidity: Int
)

data class CCWindDto(
    @SerializedName("speed") val m_speed: Double
)
