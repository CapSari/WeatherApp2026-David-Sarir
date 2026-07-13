package com.example.weatherapp2026.presentation.screens.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.presentation.components.CCCurrentWeatherHeader
import com.example.weatherapp2026.presentation.components.CCErrorView
import com.example.weatherapp2026.presentation.components.CCHomeSkeletonScreen
import com.example.weatherapp2026.presentation.components.CCWeatherBackground
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCHomeScreen(
    m_viewModel: CCHomeViewModel = hiltViewModel(),
    onNavigateToForecast: () -> Unit = {}
) {
    val m_uiState by m_viewModel.m_uiState.collectAsStateWithLifecycle()
    val m_context = LocalContext.current

    val m_permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { m_permissions ->
        val m_granted = m_permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                m_permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (m_granted) m_viewModel.onLocationPermissionGranted()
        else m_viewModel.onLocationPermissionDenied()
    }

    LaunchedEffect(Unit) {
        val m_hasFine = ContextCompat.checkSelfPermission(
            m_context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val m_hasCoarse = ContextCompat.checkSelfPermission(
            m_context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        when {
            m_hasFine || m_hasCoarse -> m_viewModel.onLocationPermissionGranted()
            else -> m_permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    when (val m_state = m_uiState) {
        is CCHomeUiState.Loading -> CCHomeSkeletonScreen()
        is CCHomeUiState.Error -> CCErrorView(
            m_message = m_state.m_message,
            onRetry = { m_viewModel.refresh() }
        )
        is CCHomeUiState.Success -> CCWeatherContent(
            m_state = m_state,
            onNavigateToForecast = onNavigateToForecast
        )
    }
}

@Composable
private fun CCWeatherContent(
    m_state: CCHomeUiState.Success,
    onNavigateToForecast: () -> Unit = {}
) {
    CCWeatherBackground(m_weatherType = m_state.m_weatherType) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            CCCurrentWeatherHeader(
                m_weather = m_state.m_currentWeather,
                m_modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            OutlinedButton(
                onClick = onNavigateToForecast,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CCTheme.spacing.screenEdge),
                border = BorderStroke(CCTheme.elevation.buttonBorderWidth, CCTheme.colors.onWeatherSurface),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CCTheme.colors.onWeatherSurface)
            ) {
                Text(text = "View 5 Day Forecast", style = CCTheme.textStyles.buttonLabel)
            }

            Spacer(modifier = Modifier.height(CCTheme.spacing.xl))
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

private fun previewWeather(
    m_iconCode: String = "01d",
    m_conditionId: Int = 800,
    m_description: String = "Clear sky",
    m_city: String = "Johannesburg"
) = CCCurrentWeather(
    m_cityName = m_city,
    m_temperature = 25.0,
    m_feelsLike = 24.0,
    m_humidity = 45,
    m_weatherCondition = "Clear",
    m_weatherDescription = m_description,
    m_iconCode = m_iconCode,
    m_windSpeed = 3.5,
    m_conditionId = m_conditionId,
    m_timestamp = 0L
)

private val previewForecast = listOf(
    CCForecastDay("2024-07-10", "Monday",    18.0, 20.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-11", "Tuesday",   14.0, 23.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-12", "Wednesday", 16.0, 27.0, "Clouds",  "04d", 804, CCWeatherType.CLOUDY),
    CCForecastDay("2024-07-13", "Thursday",  19.0, 28.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-14", "Friday",    15.0, 30.0, "Drizzle", "09d", 301, CCWeatherType.RAINY)
)

private fun previewState(m_type: CCWeatherType, m_city: String = "Johannesburg") =
    CCHomeUiState.Success(
        m_currentWeather = previewWeather(
            m_iconCode = when (m_type) {
                CCWeatherType.SUNNY -> "01d"
                CCWeatherType.CLOUDY -> "04d"
                CCWeatherType.RAINY -> "10d"
            },
            m_conditionId = when (m_type) {
                CCWeatherType.SUNNY -> 800
                CCWeatherType.CLOUDY -> 804
                CCWeatherType.RAINY -> 501
            },
            m_description = when (m_type) {
                CCWeatherType.SUNNY -> "Clear sky"
                CCWeatherType.CLOUDY -> "Overcast clouds"
                CCWeatherType.RAINY -> "Moderate rain"
            },
            m_city = m_city
        ),
        m_forecast = previewForecast,
        m_weatherType = m_type
    )

@Preview(name = "Home — Sunny", showSystemUi = true)
@Composable
private fun CCHomeScreenSunnyPreview() {
    CCWeatherTheme { CCWeatherContent(previewState(CCWeatherType.SUNNY, "Johannesburg")) }
}

@Preview(name = "Home — Cloudy", showSystemUi = true)
@Composable
private fun CCHomeScreenCloudyPreview() {
    CCWeatherTheme { CCWeatherContent(previewState(CCWeatherType.CLOUDY, "Cape Town")) }
}

@Preview(name = "Home — Rainy", showSystemUi = true)
@Composable
private fun CCHomeScreenRainyPreview() {
    CCWeatherTheme { CCWeatherContent(previewState(CCWeatherType.RAINY, "Durban")) }
}
