package com.example.weatherapp2026.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp2026.data.local.dao.CCWeatherDao
import com.example.weatherapp2026.data.local.entity.CCCurrentWeatherEntity
import com.example.weatherapp2026.data.local.entity.CCForecastEntity

@Database(
    entities = [CCCurrentWeatherEntity::class, CCForecastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CCWeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): CCWeatherDao
}
