package com.example.weatherapp2026.domain.usecase

import app.cash.turbine.test
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
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

class
CCGetForecastUseCaseTest {

    private lateinit var m_repository: CCWeatherRepository
    private lateinit var m_useCase: CCGetForecastUseCase

    @Before
    fun setup() {
        m_repository = mockk()
        m_useCase = CCGetForecastUseCase(m_repository)
    }

    @Test
    fun `invoke delegates to repository with correct coordinates`() = runTest {
        val m_lat = -33.9249
        val m_lon = 18.4241
        val m_forecast = buildForecast()

        every { m_repository.getFiveDayForecast(m_lat, m_lon) } returns
                flowOf(CCResult.Success(m_forecast))

        m_useCase(m_lat, m_lon).test {
            val m_result = awaitItem()
            assertTrue(m_result is CCResult.Success)
            assertEquals(5, (m_result as CCResult.Success).m_data.size)
            awaitComplete()
        }

        verify(exactly = 1) { m_repository.getFiveDayForecast(m_lat, m_lon) }
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        every { m_repository.getFiveDayForecast(any(), any()) } returns
                flowOf(CCResult.Success(emptyList()))

        m_useCase(0.0, 0.0).test {
            val m_result = awaitItem() as CCResult.Success
            assertTrue(m_result.m_data.isEmpty())
            awaitComplete()
        }
    }

    private fun buildForecast(): List<CCForecastDay> = (1..5).map { m_i ->
        CCForecastDay(
            m_date = "2024-07-${10 + m_i}",
            m_dayName = "Day $m_i",
            m_minTemp = 15.0 + m_i,
            m_maxTemp = 25.0 + m_i,
            m_weatherCondition = "Clear",
            m_iconCode = "01d",
            m_conditionId = 800,
            m_weatherType = CCWeatherType.SUNNY
        )
    }
}
