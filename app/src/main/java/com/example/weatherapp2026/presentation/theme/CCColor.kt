package com.example.weatherapp2026.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// Raw palette
// ---------------------------------------------------------------------------

// Weather background colours
val CCColorSunny = Color(0xFF47AB2F)
val CCColorCloudy = Color(0xFF54717A)
val CCColorRainy = Color(0xFF57575D)

// Weather surface overlays (slightly darker for forecast section)
val CCColorSunnyDark = Color(0xFF3A8C25)
val CCColorCloudyDark = Color(0xFF435B62)
val CCColorRainyDark = Color(0xFF434348)

// Text
val CCColorWhite = Color(0xFFFFFFFF)
val CCColorWhiteTranslucent = Color(0xB3FFFFFF)

// System
val CCColorBackground = Color(0xFF121212)
val CCColorSurface = Color(0xFF1E1E1E)
val CCColorPrimary = Color(0xFF47AB2F)
val CCColorOnPrimary = Color(0xFFFFFFFF)

// ---------------------------------------------------------------------------
// Semantic color tokens
// ---------------------------------------------------------------------------

data class CCColors(
    val onWeatherSurface: Color,
    val onWeatherSurfaceVariant: Color,
    val onWeatherSurfaceMuted: Color,
    val divider: Color,
    val forecastCard: Color,
    val onForecastCard: Color,
    val forecastIcon: Color
)

val CCDefaultColors = CCColors(
    onWeatherSurface = Color(0xFFFFFFFF),
    onWeatherSurfaceVariant = Color(0xD9FFFFFF),
    onWeatherSurfaceMuted = Color(0xBFFFFFFF),
    divider = Color(0x99FFFFFF),
    forecastCard = Color(0xFFFFFFFF),
    onForecastCard = Color(0xFF1A1A2E),
    forecastIcon = Color(0xFFFDB813)
)

internal val LocalCCColors = staticCompositionLocalOf<CCColors> {
    error("CCColors not provided — wrap your composable in CCWeatherTheme")
}
