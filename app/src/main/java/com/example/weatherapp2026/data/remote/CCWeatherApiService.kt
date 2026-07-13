package com.example.weatherapp2026.data.remote

import com.example.weatherapp2026.data.remote.dto.CCForecastResponseDto
import com.example.weatherapp2026.data.remote.dto.CCWeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CCWeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") m_lat: Double,
        @Query("lon") m_lon: Double,
        @Query("appid") m_apiKey: String,
        @Query("units") m_units: String
    ): CCWeatherResponseDto

    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") m_lat: Double,
        @Query("lon") m_lon: Double,
        @Query("appid") m_apiKey: String,
        @Query("units") m_units: String
    ): CCForecastResponseDto
}
