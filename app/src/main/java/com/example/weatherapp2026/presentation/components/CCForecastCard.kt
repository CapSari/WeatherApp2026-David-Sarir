package com.example.weatherapp2026.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme
import kotlin.math.roundToInt

@Composable
fun CCForecastCard(
    m_forecast: CCForecastDay,
    m_modifier: Modifier = Modifier
) {
    Card(
        modifier = m_modifier
            .fillMaxWidth()
            .padding(horizontal = CCTheme.spacing.cardEdge, vertical = CCTheme.spacing.cardGap),
        shape = CCTheme.shapes.forecastCard,
        colors = CardDefaults.cardColors(containerColor = CCTheme.colors.forecastCard),
        elevation = CardDefaults.cardElevation(defaultElevation = CCTheme.elevation.forecastCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = CCTheme.spacing.cardEdge, vertical = CCTheme.spacing.cardInner),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = m_forecast.m_dayName,
                style = CCTheme.textStyles.cardDayName
            )

            Spacer(modifier = Modifier.height(CCTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CCWeatherIcon(
                    m_iconCode = m_forecast.m_iconCode,
                    m_contentDescription = m_forecast.m_weatherCondition,
                    m_tint = CCTheme.colors.forecastIcon,
                    m_modifier = Modifier.size(CCTheme.spacing.iconCard)
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "${m_forecast.m_maxTemp.roundToInt()}",
                        style = CCTheme.textStyles.temperatureCard
                    )
                    Text(
                        text = "°",
                        style = CCTheme.textStyles.degreeSuperscript,
                        modifier = Modifier.padding(top = CCTheme.spacing.xs)
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

private val previewCards = listOf(
    CCForecastDay("2024-07-10", "Monday",    18.0, 20.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-11", "Tuesday",   14.0, 23.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-12", "Wednesday", 16.0, 27.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-13", "Thursday",  19.0, 28.0, "Clear",   "01d", 800, CCWeatherType.SUNNY),
    CCForecastDay("2024-07-14", "Friday",    15.0, 30.0, "Drizzle", "09d", 301, CCWeatherType.RAINY)
)

@Preview(name = "Forecast Card — single", showBackground = true, backgroundColor = 0xFF47AB2F)
@Composable
private fun CCForecastCardPreview() {
    CCWeatherTheme {
        CCForecastCard(m_forecast = previewCards.first())
    }
}

@Preview(name = "Forecast Cards — all 5", showBackground = true, backgroundColor = 0xFF47AB2F)
@Composable
private fun CCForecastCardsAllPreview() {
    CCWeatherTheme {
        Column {
            previewCards.forEach { CCForecastCard(m_forecast = it) }
        }
    }
}
