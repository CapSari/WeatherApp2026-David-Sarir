package com.example.weatherapp2026.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp2026.data.local.entity.CCCurrentWeatherEntity
import com.example.weatherapp2026.data.local.entity.CCForecastEntity

@Dao
interface CCWeatherDao {

    @Query("SELECT * FROM current_weather WHERE m_id = 1")
    suspend fun getCurrentWeather(): CCCurrentWeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCurrentWeather(m_entity: CCCurrentWeatherEntity)

    @Query("SELECT * FROM forecast ORDER BY m_date ASC LIMIT 5")
    suspend fun getForecast(): List<CCForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertForecast(m_entities: List<CCForecastEntity>)

    @Query("DELETE FROM forecast")
    suspend fun clearForecast()
}
