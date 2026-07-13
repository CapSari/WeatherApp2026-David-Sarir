package com.example.weatherapp2026.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CCSpacing(
    // Raw scale
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    // Semantic
    val screenEdge: Dp = 32.dp,
    val cardEdge: Dp = 16.dp,
    val cardGap: Dp = 5.dp,
    val cardInner: Dp = 12.dp,
    val iconHeader: Dp = 80.dp,
    val iconCard: Dp = 36.dp
)

val CCDefaultSpacing = CCSpacing()

internal val LocalCCSpacing = staticCompositionLocalOf<CCSpacing> {
    error("CCSpacing not provided — wrap your composable in CCWeatherTheme")
}
