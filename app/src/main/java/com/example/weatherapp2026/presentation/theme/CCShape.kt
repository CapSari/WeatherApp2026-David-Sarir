package com.example.weatherapp2026.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CCShapes(
    val forecastCard: Shape = RoundedCornerShape(16.dp)
)

data class CCElevation(
    val forecastCard: Dp = 2.dp,
    val dividerThickness: Dp = 1.dp,
    val buttonBorderWidth: Dp = 1.5.dp
)

val CCDefaultShapes = CCShapes()
val CCDefaultElevation = CCElevation()

internal val LocalCCShapes = staticCompositionLocalOf<CCShapes> {
    error("CCShapes not provided — wrap your composable in CCWeatherTheme")
}

internal val LocalCCElevation = staticCompositionLocalOf<CCElevation> {
    error("CCElevation not provided — wrap your composable in CCWeatherTheme")
}
