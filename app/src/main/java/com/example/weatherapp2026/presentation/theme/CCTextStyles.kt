package com.example.weatherapp2026.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class CCTextStyles(
    // On weather background (white text)
    val temperatureDisplay: TextStyle,
    val weatherDescription: TextStyle,
    val cityName: TextStyle,
    val dateLabel: TextStyle,
    val bodyDetail: TextStyle,
    val screenTitle: TextStyle,
    val buttonLabel: TextStyle,
    // On forecast card (navy text)
    val cardDayName: TextStyle,
    val temperatureCard: TextStyle,
    val degreeSuperscript: TextStyle
)

fun buildCCTextStyles(colors: CCColors) = CCTextStyles(
    temperatureDisplay = TextStyle(fontSize = 56.sp, fontWeight = FontWeight.Bold, color = colors.onWeatherSurface),
    weatherDescription = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.onWeatherSurface),
    cityName = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.onWeatherSurface),
    dateLabel = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = colors.onWeatherSurfaceVariant),
    bodyDetail = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, color = colors.onWeatherSurfaceMuted),
    screenTitle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.onWeatherSurface),
    buttonLabel = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colors.onWeatherSurface),
    cardDayName = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.onForecastCard),
    temperatureCard = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = colors.onForecastCard),
    degreeSuperscript = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.onForecastCard)
)

val CCDefaultTextStyles = buildCCTextStyles(CCDefaultColors)

internal val LocalCCTextStyles = staticCompositionLocalOf<CCTextStyles> {
    error("CCTextStyles not provided — wrap your composable in CCWeatherTheme")
}
