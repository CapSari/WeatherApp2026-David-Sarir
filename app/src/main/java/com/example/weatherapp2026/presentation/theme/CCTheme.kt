package com.example.weatherapp2026.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private val CCDarkColorScheme = darkColorScheme(
    primary = CCColorPrimary,
    onPrimary = CCColorOnPrimary,
    background = CCColorBackground,
    surface = CCColorSurface
)

@Composable
fun CCWeatherTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalCCColors provides CCDefaultColors,
        LocalCCSpacing provides CCDefaultSpacing,
        LocalCCTextStyles provides CCDefaultTextStyles,
        LocalCCShapes provides CCDefaultShapes,
        LocalCCElevation provides CCDefaultElevation
    ) {
        MaterialTheme(
            colorScheme = CCDarkColorScheme,
            typography = CCTypography,
            content = content
        )
    }
}

object CCTheme {
    val colors: CCColors
        @Composable @ReadOnlyComposable get() = LocalCCColors.current
    val spacing: CCSpacing
        @Composable @ReadOnlyComposable get() = LocalCCSpacing.current
    val textStyles: CCTextStyles
        @Composable @ReadOnlyComposable get() = LocalCCTextStyles.current
    val shapes: CCShapes
        @Composable @ReadOnlyComposable get() = LocalCCShapes.current
    val elevation: CCElevation
        @Composable @ReadOnlyComposable get() = LocalCCElevation.current
}
