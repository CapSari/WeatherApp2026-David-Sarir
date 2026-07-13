package com.example.weatherapp2026.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.domain.usecase.CCGetCurrentWeatherUseCase
import com.example.weatherapp2026.domain.usecase.CCGetForecastUseCase
import com.example.weatherapp2026.util.CCConstants
import com.example.weatherapp2026.util.CCLocationHelper
import com.example.weatherapp2026.util.CCResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CCHomeViewModel @Inject constructor(
    private val m_getCurrentWeatherUseCase: CCGetCurrentWeatherUseCase,
    private val m_getForecastUseCase: CCGetForecastUseCase,
    private val m_locationHelper: CCLocationHelper
) : ViewModel() {

    private val _m_uiState = MutableStateFlow<CCHomeUiState>(CCHomeUiState.Loading)
    val m_uiState: StateFlow<CCHomeUiState> = _m_uiState.asStateFlow()

    fun onLocationPermissionGranted() {
        loadWeather()
    }

    fun onLocationPermissionDenied() {
        Timber.w("Location permission denied — falling back to default coordinates")
        loadWeatherForCoordinates(CCConstants.DEFAULT_LAT, CCConstants.DEFAULT_LON)
    }

    fun loadWeather() {
        viewModelScope.launch {
            // 5-second window to get a real GPS fix; falls back to Johannesburg defaults
            // if the emulator has no signal or the device is slow to acquire.
            val m_location = withTimeoutOrNull(5_000L) {
                m_locationHelper.getCurrentLocation().first()
            }
            val m_lat = m_location?.latitude ?: CCConstants.DEFAULT_LAT
            val m_lon = m_location?.longitude ?: CCConstants.DEFAULT_LON
            Timber.d("Loading weather for lat=$m_lat, lon=$m_lon")
            loadWeatherForCoordinates(m_lat, m_lon)
        }
    }

    fun refresh() {
        _m_uiState.value = CCHomeUiState.Loading
        loadWeather()
    }

    private fun loadWeatherForCoordinates(m_lat: Double, m_lon: Double) {
        viewModelScope.launch {
            var m_latestWeather: CCCurrentWeather? = null
            var m_latestForecast: List<CCForecastDay> = emptyList()
            var m_isFromCache = false

            combine(
                m_getCurrentWeatherUseCase(m_lat, m_lon),
                m_getForecastUseCase(m_lat, m_lon)
            ) { m_weatherResult, m_forecastResult ->
                Pair(m_weatherResult, m_forecastResult)
            }.collect { (m_weatherResult, m_forecastResult) ->
                when (m_weatherResult) {
                    is CCResult.Success -> {
                        m_latestWeather = m_weatherResult.m_data
                        m_isFromCache = false
                    }
                    is CCResult.Error -> {
                        if (m_latestWeather == null) {
                            Timber.e("Current weather error: ${m_weatherResult.m_message}")
                            _m_uiState.value = CCHomeUiState.Error(m_weatherResult.m_message)
                            return@collect
                        }
                    }
                    CCResult.Loading -> Unit
                }

                when (m_forecastResult) {
                    is CCResult.Success -> m_latestForecast = m_forecastResult.m_data
                    is CCResult.Error -> {
                        if (m_latestForecast.isEmpty()) {
                            Timber.e("Forecast error: ${m_forecastResult.m_message}")
                            _m_uiState.value = CCHomeUiState.Error(m_forecastResult.m_message)
                            return@collect
                        }
                    }
                    CCResult.Loading -> Unit
                }

                val m_weather = m_latestWeather
                if (m_weather != null && m_latestForecast.isNotEmpty()) {
                    _m_uiState.value = CCHomeUiState.Success(
                        m_currentWeather = m_weather,
                        m_forecast = m_latestForecast,
                        m_weatherType = CCWeatherType.from(m_weather.m_conditionId),
                        m_isFromCache = m_isFromCache
                    )
                }
            }
        }
    }
}
