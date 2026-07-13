package com.example.weatherapp2026.presentation.screens.forecast

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.presentation.components.CCForecastCard
import com.example.weatherapp2026.presentation.components.CCWeatherBackground
import com.example.weatherapp2026.presentation.screens.home.CCHomeUiState
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCForecastScreen(
    m_state: CCHomeUiState.Success,
    onBack: () -> Unit = {}
) {
    CCWeatherBackground(m_weatherType = m_state.m_weatherType, m_useSunnyVariant = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = CCTheme.spacing.sm, bottom = CCTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = CCTheme.colors.onWeatherSurface
                    )
                }
                Text(
                    text = "5 Day Forecast",
                    style = CCTheme.textStyles.screenTitle
                )
            }

            HorizontalDivider(
                color = CCTheme.colors.divider,
                thickness = CCTheme.elevation.dividerThickness
            )

            Spacer(modifier = Modifier.height(CCTheme.spacing.sm))

            m_state.m_forecast.forEachIndexed { m_index, m_day ->
                val m_anim = remember { Animatable(0f) }
                LaunchedEffect(m_index) {
                    delay(m_index * 80L)
                    m_anim.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 300))
                }
                CCForecastCard(
                    m_forecast = m_day,
                    m_modifier = Modifier.weight(1f).alpha(m_anim.value)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

private val previewForecast = listOf(
    CCForecastDay("2024-07-10", "Monday",    18.0, 20.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-11", "Tuesday",   14.0, 23.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-12", "Wednesday", 16.0, 27.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-13", "Thursday",  19.0, 28.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-14", "Friday",    15.0, 30.0, "Drizzle", "09d", 301, CCWeatherType.RAINY)
)

private fun previewState(m_weatherType: CCWeatherType) = CCHomeUiState.Success(
    m_currentWeather = CCCurrentWeather(
        m_cityName = "Johannesburg",
        m_temperature = 25.0,
        m_feelsLike = 24.0,
        m_humidity = 45,
        m_weatherCondition = "Clear",
        m_weatherDescription = "Clear sky",
        m_iconCode = "01d",
        m_windSpeed = 3.5,
        m_conditionId = 800,
        m_timestamp = 0L
    ),
    m_forecast = previewForecast,
    m_weatherType = m_weatherType
)

@Preview(name = "Forecast — Sunny", showSystemUi = true)
@Composable
private fun CCForecastScreenSunnyPreview() {
    CCWeatherTheme { CCForecastScreen(m_state = previewState(CCWeatherType.SUNNY)) }
}

@Preview(name = "Forecast — Rainy", showSystemUi = true)
@Composable
private fun CCForecastScreenRainyPreview() {
    CCWeatherTheme { CCForecastScreen(m_state = previewState(CCWeatherType.RAINY)) }
}

@Preview(name = "Forecast — Cloudy", showSystemUi = true)
@Composable
private fun CCForecastScreenCloudyPreview() {
    CCWeatherTheme { CCForecastScreen(m_state = previewState(CCWeatherType.CLOUDY)) }
}
