package com.example.weatherapp2026.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCWeatherIcon(
    m_iconCode: String,
    m_contentDescription: String,
    m_tint: Color = CCTheme.colors.onWeatherSurface,
    m_modifier: Modifier = Modifier
) {
    Icon(
        imageVector = resolveWeatherIcon(m_iconCode),
        contentDescription = m_contentDescription,
        tint = m_tint,
        modifier = m_modifier
    )
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(name = "Weather Icons — all types", showBackground = true, backgroundColor = 0xFF47AB2F)
@Composable
private fun CCWeatherIconPreview() {
    CCWeatherTheme {
        val m_icons = listOf(
            "01d" to "Clear sky",
            "02d" to "Few clouds",
            "04d" to "Broken clouds",
            "09d" to "Rain",
            "11d" to "Thunderstorm",
            "13d" to "Snow",
            "50d" to "Mist"
        )
        Row(
            modifier = Modifier.padding(CCTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(CCTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            m_icons.forEach { (m_code, m_desc) ->
                CCWeatherIcon(
                    m_iconCode = m_code,
                    m_contentDescription = m_desc,
                    m_modifier = Modifier.size(CCTheme.spacing.iconCard)
                )
            }
        }
    }
}

fun resolveWeatherIcon(m_iconCode: String): ImageVector = when {
    m_iconCode.startsWith("01") -> Icons.Filled.WbSunny
    m_iconCode.startsWith("02") -> Icons.Filled.WbCloudy
    m_iconCode.startsWith("03") || m_iconCode.startsWith("04") -> Icons.Filled.Cloud
    m_iconCode.startsWith("09") || m_iconCode.startsWith("10") -> Icons.Filled.WaterDrop
    m_iconCode.startsWith("11") -> Icons.Filled.Thunderstorm
    m_iconCode.startsWith("13") -> Icons.Filled.AcUnit
    m_iconCode.startsWith("50") -> Icons.Filled.WbCloudy
    else -> Icons.Filled.WbSunny
}
