package com.example.weatherapp2026.data.mapper

import com.example.weatherapp2026.data.local.entity.CCCurrentWeatherEntity
import com.example.weatherapp2026.data.local.entity.CCForecastEntity
import com.example.weatherapp2026.data.remote.dto.CCCityDto
import com.example.weatherapp2026.data.remote.dto.CCCoordDto
import com.example.weatherapp2026.data.remote.dto.CCForecastItemDto
import com.example.weatherapp2026.data.remote.dto.CCForecastResponseDto
import com.example.weatherapp2026.data.remote.dto.CCMainDto
import com.example.weatherapp2026.data.remote.dto.CCWeatherConditionDto
import com.example.weatherapp2026.data.remote.dto.CCWeatherResponseDto
import com.example.weatherapp2026.data.remote.dto.CCWindDto
import com.example.weatherapp2026.domain.model.CCWeatherType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CCWeatherMapperTest {

    private lateinit var m_mapper: CCWeatherMapper

    @Before
    fun setup() {
        m_mapper = CCWeatherMapper()
    }

    @Test
    fun `mapResponseToEntity maps all fields correctly`() {
        val m_dto = buildWeatherResponseDto(conditionId = 800, description = "clear sky")

        val m_result = m_mapper.mapResponseToEntity(m_dto)

        assertEquals(1, m_result.m_id)
        assertEquals("Johannesburg", m_result.m_cityName)
        assertEquals(25.0, m_result.m_temperature, 0.01)
        assertEquals(24.0, m_result.m_feelsLike, 0.01)
        assertEquals(45, m_result.m_humidity)
        assertEquals("Clear", m_result.m_weatherCondition)
        assertEquals("Clear sky", m_result.m_weatherDescription)
        assertEquals("01d", m_result.m_iconCode)
        assertEquals(3.5, m_result.m_windSpeed, 0.01)
        assertEquals(800, m_result.m_conditionId)
    }

    @Test
    fun `mapEntityToCurrentWeather maps all fields correctly`() {
        val m_entity = CCCurrentWeatherEntity(
            m_id = 1,
            m_cityName = "Cape Town",
            m_temperature = 22.0,
            m_feelsLike = 21.0,
            m_humidity = 60,
            m_weatherCondition = "Clouds",
            m_weatherDescription = "Overcast clouds",
            m_iconCode = "04d",
            m_windSpeed = 5.0,
            m_conditionId = 804,
            m_timestamp = 1000L
        )

        val m_result = m_mapper.mapEntityToCurrentWeather(m_entity)

        assertEquals("Cape Town", m_result.m_cityName)
        assertEquals(22.0, m_result.m_temperature, 0.01)
        assertEquals(804, m_result.m_conditionId)
    }

    @Test
    fun `mapForecastResponseToEntities groups by date and takes first 5`() {
        val m_items = buildForecastItems()
        val m_dto = CCForecastResponseDto(
            m_list = m_items,
            m_city = CCCityDto("Durban", "ZA")
        )

        val m_result = m_mapper.mapForecastResponseToEntities(m_dto)

        assertTrue("Should have at most 5 days", m_result.size <= 5)
        val m_dates = m_result.map { it.m_date }.toSet()
        assertEquals("Each date should be unique", m_result.size, m_dates.size)
    }

    @Test
    fun `mapForecastResponseToEntities calculates min and max temp correctly`() {
        val m_items = listOf(
            buildForecastItemDto("2024-07-10 06:00:00", temp = 18.0, tempMin = 16.0, tempMax = 20.0),
            buildForecastItemDto("2024-07-10 12:00:00", temp = 25.0, tempMin = 22.0, tempMax = 27.0),
            buildForecastItemDto("2024-07-10 18:00:00", temp = 23.0, tempMin = 21.0, tempMax = 25.0)
        )
        val m_dto = CCForecastResponseDto(
            m_list = m_items,
            m_city = CCCityDto("Test City", "ZA")
        )

        val m_result = m_mapper.mapForecastResponseToEntities(m_dto)

        assertEquals(1, m_result.size)
        assertEquals(16.0, m_result[0].m_minTemp, 0.01)
        assertEquals(27.0, m_result[0].m_maxTemp, 0.01)
    }

    @Test
    fun `mapEntityToForecastDay derives weather type from condition id`() {
        val m_sunnyEntity = CCForecastEntity("2024-07-10", "Wednesday", 18.0, 27.0, "Clear", "01d", 800)
        val m_rainyEntity = CCForecastEntity("2024-07-11", "Thursday", 14.0, 20.0, "Rain", "10d", 500)
        val m_cloudyEntity = CCForecastEntity("2024-07-12", "Friday", 15.0, 22.0, "Clouds", "04d", 804)

        assertEquals(CCWeatherType.SUNNY, m_mapper.mapEntityToForecastDay(m_sunnyEntity).m_weatherType)
        assertEquals(CCWeatherType.RAINY, m_mapper.mapEntityToForecastDay(m_rainyEntity).m_weatherType)
        assertEquals(CCWeatherType.CLOUDY, m_mapper.mapEntityToForecastDay(m_cloudyEntity).m_weatherType)
    }

    // --- Helpers ---

    private fun buildWeatherResponseDto(conditionId: Int = 800, description: String = "clear sky") =
        CCWeatherResponseDto(
            m_coord = CCCoordDto(-26.2023, 28.0436),
            m_weather = listOf(CCWeatherConditionDto(conditionId, "Clear", description, "01d")),
            m_main = CCMainDto(25.0, 24.0, 22.0, 27.0, 45),
            m_wind = CCWindDto(3.5),
            m_name = "Johannesburg",
            m_dt = 1720000000L
        )

    private fun buildForecastItems(): List<CCForecastItemDto> {
        val m_dates = listOf(
            "2024-07-10 12:00:00",
            "2024-07-11 12:00:00",
            "2024-07-12 12:00:00",
            "2024-07-13 12:00:00",
            "2024-07-14 12:00:00",
            "2024-07-15 12:00:00"
        )
        return m_dates.map { buildForecastItemDto(it) }
    }

    private fun buildForecastItemDto(
        m_dtTxt: String,
        temp: Double = 22.0,
        tempMin: Double = 18.0,
        tempMax: Double = 26.0,
        conditionId: Int = 800
    ) = CCForecastItemDto(
        m_dt = 1720000000L,
        m_main = CCMainDto(temp, temp - 2, tempMin, tempMax, 50),
        m_weather = listOf(CCWeatherConditionDto(conditionId, "Clear", "clear sky", "01d")),
        m_dtTxt = m_dtTxt
    )
}
