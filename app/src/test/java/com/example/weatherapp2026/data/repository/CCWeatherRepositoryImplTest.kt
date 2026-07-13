package com.example.weatherapp2026.data.repository

import com.example.weatherapp2026.data.local.dao.CCWeatherDao
import com.example.weatherapp2026.data.local.entity.CCCurrentWeatherEntity
import com.example.weatherapp2026.data.local.entity.CCForecastEntity
import com.example.weatherapp2026.data.mapper.CCWeatherMapper
import com.example.weatherapp2026.data.remote.CCWeatherApiService
import com.example.weatherapp2026.data.remote.dto.CCCityDto
import com.example.weatherapp2026.data.remote.dto.CCCoordDto
import com.example.weatherapp2026.data.remote.dto.CCForecastItemDto
import com.example.weatherapp2026.data.remote.dto.CCForecastResponseDto
import com.example.weatherapp2026.data.remote.dto.CCMainDto
import com.example.weatherapp2026.data.remote.dto.CCWeatherConditionDto
import com.example.weatherapp2026.data.remote.dto.CCWeatherResponseDto
import com.example.weatherapp2026.data.remote.dto.CCWindDto
import com.example.weatherapp2026.util.CCNetworkMonitor
import com.example.weatherapp2026.util.CCResult
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CCWeatherRepositoryImplTest {

    private lateinit var m_apiService: CCWeatherApiService
    private lateinit var m_weatherDao: CCWeatherDao
    private lateinit var m_mapper: CCWeatherMapper
    private lateinit var m_networkMonitor: CCNetworkMonitor
    private lateinit var m_repository: CCWeatherRepositoryImpl

    @Before
    fun setup() {
        m_apiService = mockk()
        m_weatherDao = mockk(relaxed = true)
        m_mapper = CCWeatherMapper()
        m_networkMonitor = mockk()
        m_repository = CCWeatherRepositoryImpl(m_apiService, m_weatherDao, m_mapper, m_networkMonitor)
    }

    @Test
    fun `getCurrentWeather emits cached data first then fresh data`() = runTest {
        val m_cached = buildCurrentWeatherEntity()
        val m_apiResponse = buildWeatherResponseDto()

        coEvery { m_weatherDao.getCurrentWeather() } returns m_cached
        every { m_networkMonitor.isConnected() } returns true
        coEvery { m_apiService.getCurrentWeather(any(), any(), any(), any()) } returns m_apiResponse

        m_repository.getCurrentWeather(-26.2023, 28.0436).test {
            assertTrue(awaitItem() is CCResult.Loading)

            val m_cachedResult = awaitItem()
            assertTrue(m_cachedResult is CCResult.Success)
            assertEquals("Cached City", (m_cachedResult as CCResult.Success).m_data.m_cityName)

            val m_freshResult = awaitItem()
            assertTrue(m_freshResult is CCResult.Success)
            assertEquals("Johannesburg", (m_freshResult as CCResult.Success).m_data.m_cityName)

            awaitComplete()
        }

        coVerify { m_weatherDao.upsertCurrentWeather(any()) }
    }

    @Test
    fun `getCurrentWeather emits Error when no cache and no network`() = runTest {
        coEvery { m_weatherDao.getCurrentWeather() } returns null
        every { m_networkMonitor.isConnected() } returns false

        m_repository.getCurrentWeather(0.0, 0.0).test {
            assertTrue(awaitItem() is CCResult.Loading)
            assertTrue(awaitItem() is CCResult.Error)
            awaitComplete()
        }
    }

    @Test
    fun `getFiveDayForecast clears and reinserts on fresh fetch`() = runTest {
        coEvery { m_weatherDao.getForecast() } returns emptyList()
        every { m_networkMonitor.isConnected() } returns true
        coEvery { m_apiService.getFiveDayForecast(any(), any(), any(), any()) } returns buildForecastResponseDto()

        m_repository.getFiveDayForecast(-26.2023, 28.0436).test {
            awaitItem() // Loading
            val m_result = awaitItem()
            assertTrue(m_result is CCResult.Success)
            awaitComplete()
        }

        coVerify { m_weatherDao.clearForecast() }
        coVerify { m_weatherDao.upsertForecast(any()) }
    }

    @Test
    fun `getFiveDayForecast serves cache when offline`() = runTest {
        val m_cachedForecast = listOf(buildForecastEntity())
        coEvery { m_weatherDao.getForecast() } returns m_cachedForecast
        every { m_networkMonitor.isConnected() } returns false

        m_repository.getFiveDayForecast(0.0, 0.0).test {
            awaitItem() // Loading
            val m_result = awaitItem()
            assertTrue(m_result is CCResult.Success)
            assertEquals(1, (m_result as CCResult.Success).m_data.size)
            awaitComplete()
        }
    }

    // --- Helpers ---

    private fun buildCurrentWeatherEntity() = CCCurrentWeatherEntity(
        m_id = 1,
        m_cityName = "Cached City",
        m_temperature = 20.0,
        m_feelsLike = 19.0,
        m_humidity = 50,
        m_weatherCondition = "Clouds",
        m_weatherDescription = "Overcast clouds",
        m_iconCode = "04d",
        m_windSpeed = 4.0,
        m_conditionId = 804,
        m_timestamp = 1000L
    )

    private fun buildForecastEntity() = CCForecastEntity(
        m_date = "2024-07-10",
        m_dayName = "Wednesday",
        m_minTemp = 16.0,
        m_maxTemp = 24.0,
        m_weatherCondition = "Clear",
        m_iconCode = "01d",
        m_conditionId = 800
    )

    private fun buildWeatherResponseDto() = CCWeatherResponseDto(
        m_coord = CCCoordDto(-26.2023, 28.0436),
        m_weather = listOf(CCWeatherConditionDto(800, "Clear", "clear sky", "01d")),
        m_main = CCMainDto(25.0, 24.0, 22.0, 27.0, 45),
        m_wind = CCWindDto(3.5),
        m_name = "Johannesburg",
        m_dt = 1720000000L
    )

    private fun buildForecastResponseDto() = CCForecastResponseDto(
        m_list = listOf(
            CCForecastItemDto(
                m_dt = 1720000000L,
                m_main = CCMainDto(22.0, 21.0, 18.0, 26.0, 55),
                m_weather = listOf(CCWeatherConditionDto(800, "Clear", "clear sky", "01d")),
                m_dtTxt = "2024-07-10 12:00:00"
            )
        ),
        m_city = CCCityDto("Johannesburg", "ZA")
    )
}
