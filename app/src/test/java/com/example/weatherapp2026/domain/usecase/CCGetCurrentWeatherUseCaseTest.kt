package com.example.weatherapp2026.domain.usecase

import app.cash.turbine.test
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.repository.CCWeatherRepository
import com.example.weatherapp2026.util.CCResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CCGetCurrentWeatherUseCaseTest {

    private lateinit var m_repository: CCWeatherRepository
    private lateinit var m_useCase: CCGetCurrentWeatherUseCase

    @Before
    fun setup() {
        m_repository = mockk()
        m_useCase = CCGetCurrentWeatherUseCase(m_repository)
    }

    @Test
    fun `invoke delegates to repository with correct coordinates`() = runTest {
        val m_lat = -26.2023
        val m_lon = 28.0436
        val m_expected = buildCurrentWeather()

        every { m_repository.getCurrentWeather(m_lat, m_lon) } returns
                flowOf(CCResult.Loading, CCResult.Success(m_expected))

        m_useCase(m_lat, m_lon).test {
            assertTrue(awaitItem() is CCResult.Loading)
            val m_result = awaitItem()
            assertTrue(m_result is CCResult.Success)
            assertEquals(m_expected, (m_result as CCResult.Success).m_data)
            awaitComplete()
        }

        verify(exactly = 1) { m_repository.getCurrentWeather(m_lat, m_lon) }
    }

    @Test
    fun `invoke propagates error from repository`() = runTest {
        val m_errorMessage = "Network error"
        every { m_repository.getCurrentWeather(any(), any()) } returns
                flowOf(CCResult.Loading, CCResult.Error(m_errorMessage))

        m_useCase(0.0, 0.0).test {
            awaitItem() // Loading
            val m_result = awaitItem()
            assertTrue(m_result is CCResult.Error)
            assertEquals(m_errorMessage, (m_result as CCResult.Error).m_message)
            awaitComplete()
        }
    }

    private fun buildCurrentWeather() = CCCurrentWeather(
        m_cityName = "Johannesburg",
        m_temperature = 25.0,
        m_feelsLike = 24.0,
        m_humidity = 45,
        m_weatherCondition = "Clear",
        m_weatherDescription = "Clear sky",
        m_iconCode = "01d",
        m_windSpeed = 3.5,
        m_conditionId = 800,
        m_timestamp = 1720000000L
    )
}
