package com.example.weatherapp2026.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CCForecastResponseDto(
    @SerializedName("list") val m_list: List<CCForecastItemDto>,
    @SerializedName("city") val m_city: CCCityDto
)

data class CCForecastItemDto(
    @SerializedName("dt") val m_dt: Long,
    @SerializedName("main") val m_main: CCMainDto,
    @SerializedName("weather") val m_weather: List<CCWeatherConditionDto>,
    @SerializedName("dt_txt") val m_dtTxt: String
)

data class CCCityDto(
    @SerializedName("name") val m_name: String,
    @SerializedName("country") val m_country: String
)
