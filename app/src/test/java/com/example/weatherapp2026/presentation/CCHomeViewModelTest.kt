package com.example.weatherapp2026.presentation

import android.location.Location
import app.cash.turbine.test
import com.example.weatherapp2026.CCTestCoroutineRule
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.domain.usecase.CCGetCurrentWeatherUseCase
import com.example.weatherapp2026.domain.usecase.CCGetForecastUseCase
import com.example.weatherapp2026.presentation.screens.home.CCHomeUiState
import com.example.weatherapp2026.presentation.screens.home.CCHomeViewModel
import com.example.weatherapp2026.util.CCLocationHelper
import com.example.weatherapp2026.util.CCResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CCHomeViewModelTest {

    @get:Rule
    val m_coroutineRule = CCTestCoroutineRule()

    private lateinit var m_getCurrentWeatherUseCase: CCGetCurrentWeatherUseCase
    private lateinit var m_getForecastUseCase: CCGetForecastUseCase
    private lateinit var m_locationHelper: CCLocationHelper
    private lateinit var m_viewModel: CCHomeViewModel

    @Before
    fun setup() {
        m_getCurrentWeatherUseCase = mockk()
        m_getForecastUseCase = mockk()
        m_locationHelper = mockk()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        m_viewModel = CCHomeViewModel(
            m_getCurrentWeatherUseCase,
            m_getForecastUseCase,
            m_locationHelper
        )
        assertTrue(m_viewModel.m_uiState.value is CCHomeUiState.Loading)
    }

    @Test
    fun `onLocationPermissionGranted loads weather and emits Success`() = runTest {
        val m_weather = buildCurrentWeather()
        val m_forecast = buildForecast()
        val m_location = mockk<Location> {
            every { latitude } returns -26.2023
            every { longitude } returns 28.0436
        }

        every { m_locationHelper.getCurrentLocation() } returns flowOf(m_location)
        every { m_getCurrentWeatherUseCase(any(), any()) } returns flowOf(CCResult.Success(m_weather))
        every { m_getForecastUseCase(any(), any()) } returns flowOf(CCResult.Success(m_forecast))

        m_viewModel = CCHomeViewModel(
            m_getCurrentWeatherUseCase,
            m_getForecastUseCase,
            m_locationHelper
        )

        m_viewModel.m_uiState.test {
            awaitItem() // initial Loading

            m_viewModel.onLocationPermissionGranted()
            advanceUntilIdle()

            val m_state = awaitItem()
            assertTrue("Expected Success but got $m_state", m_state is CCHomeUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLocationPermissionDenied falls back to default location`() = runTest {
        val m_weather = buildCurrentWeather()
        val m_forecast = buildForecast()

        every { m_getCurrentWeatherUseCase(any(), any()) } returns flowOf(CCResult.Success(m_weather))
        every { m_getForecastUseCase(any(), any()) } returns flowOf(CCResult.Success(m_forecast))

        m_viewModel = CCHomeViewModel(
            m_getCurrentWeatherUseCase,
            m_getForecastUseCase,
            m_locationHelper
        )

        m_viewModel.m_uiState.test {
            awaitItem() // initial Loading
            m_viewModel.onLocationPermissionDenied()
            advanceUntilIdle()

            val m_state = awaitItem()
            assertTrue("Expected Success but got $m_state", m_state is CCHomeUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `weather error emits Error state when no cache available`() = runTest {
        val m_location = mockk<Location> {
            every { latitude } returns -26.2023
            every { longitude } returns 28.0436
        }

        every { m_locationHelper.getCurrentLocation() } returns flowOf(m_location)
        every { m_getCurrentWeatherUseCase(any(), any()) } returns
                flowOf(CCResult.Loading, CCResult.Error("Network error"))
        every { m_getForecastUseCase(any(), any()) } returns
                flowOf(CCResult.Loading, CCResult.Error("Network error"))

        m_viewModel = CCHomeViewModel(
            m_getCurrentWeatherUseCase,
            m_getForecastUseCase,
            m_locationHelper
        )

        m_viewModel.m_uiState.test {
            awaitItem() // initial Loading
            m_viewModel.onLocationPermissionGranted()
            advanceUntilIdle()

            val m_state = awaitItem()
            assertTrue("Expected Error but got $m_state", m_state is CCHomeUiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // --- Helpers ---

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

    private fun buildForecast(): List<CCForecastDay> = (1..5).map { m_i ->
        CCForecastDay(
            m_date = "2024-07-${10 + m_i}",
            m_dayName = "Day $m_i",
            m_minTemp = 15.0,
            m_maxTemp = 25.0,
            m_weatherCondition = "Clear",
            m_iconCode = "01d",
            m_conditionId = 800,
            m_weatherType = CCWeatherType.SUNNY
        )
    }
}
