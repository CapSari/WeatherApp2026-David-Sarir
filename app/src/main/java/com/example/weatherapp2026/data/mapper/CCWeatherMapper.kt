package com.example.weatherapp2026.data.mapper

import com.example.weatherapp2026.data.local.entity.CCCurrentWeatherEntity
import com.example.weatherapp2026.data.local.entity.CCForecastEntity
import com.example.weatherapp2026.data.remote.dto.CCForecastResponseDto
import com.example.weatherapp2026.data.remote.dto.CCWeatherResponseDto
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CCWeatherMapper @Inject constructor() {

    fun mapResponseToEntity(m_dto: CCWeatherResponseDto): CCCurrentWeatherEntity {
        val m_condition = m_dto.m_weather.firstOrNull()
        return CCCurrentWeatherEntity(
            m_id = 1,
            m_cityName = m_dto.m_name,
            m_temperature = m_dto.m_main.m_temp,
            m_feelsLike = m_dto.m_main.m_feelsLike,
            m_humidity = m_dto.m_main.m_humidity,
            m_weatherCondition = m_condition?.m_main.orEmpty(),
            m_weatherDescription = m_condition?.m_description.orEmpty()
                .replaceFirstChar { it.uppercase() },
            m_iconCode = m_condition?.m_icon.orEmpty(),
            m_windSpeed = m_dto.m_wind.m_speed,
            m_conditionId = m_condition?.m_id ?: 800,
            m_timestamp = m_dto.m_dt
        )
    }

    fun mapEntityToCurrentWeather(m_entity: CCCurrentWeatherEntity): CCCurrentWeather =
        CCCurrentWeather(
            m_cityName = m_entity.m_cityName,
            m_temperature = m_entity.m_temperature,
            m_feelsLike = m_entity.m_feelsLike,
            m_humidity = m_entity.m_humidity,
            m_weatherCondition = m_entity.m_weatherCondition,
            m_weatherDescription = m_entity.m_weatherDescription,
            m_iconCode = m_entity.m_iconCode,
            m_windSpeed = m_entity.m_windSpeed,
            m_conditionId = m_entity.m_conditionId,
            m_timestamp = m_entity.m_timestamp
        )

    fun mapForecastResponseToEntities(m_dto: CCForecastResponseDto): List<CCForecastEntity> =
        m_dto.m_list
            .groupBy { it.m_dtTxt.take(10) }
            .entries
            .take(5)
            .map { (m_date, m_items) ->
                val m_representative = m_items.firstOrNull { it.m_dtTxt.contains("12:00:00") }
                    ?: m_items.first()
                val m_condition = m_representative.m_weather.firstOrNull()
                CCForecastEntity(
                    m_date = m_date,
                    m_dayName = formatDayName(m_date),
                    m_minTemp = m_items.minOf { it.m_main.m_tempMin },
                    m_maxTemp = m_items.maxOf { it.m_main.m_tempMax },
                    m_weatherCondition = m_condition?.m_main.orEmpty(),
                    m_iconCode = m_condition?.m_icon.orEmpty(),
                    m_conditionId = m_condition?.m_id ?: 800
                )
            }

    fun mapEntityToForecastDay(m_entity: CCForecastEntity): CCForecastDay =
        CCForecastDay(
            m_date = m_entity.m_date,
            m_dayName = m_entity.m_dayName,
            m_minTemp = m_entity.m_minTemp,
            m_maxTemp = m_entity.m_maxTemp,
            m_weatherCondition = m_entity.m_weatherCondition,
            m_iconCode = m_entity.m_iconCode,
            m_conditionId = m_entity.m_conditionId,
            m_weatherType = CCWeatherType.from(m_entity.m_conditionId)
        )

    private fun formatDayName(m_dateStr: String): String = try {
        val m_inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val m_outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val m_parsed = m_inputFormat.parse(m_dateStr)
        if (m_parsed != null) m_outputFormat.format(m_parsed) else m_dateStr
    } catch (m_e: ParseException) {
        m_dateStr
    }
}
