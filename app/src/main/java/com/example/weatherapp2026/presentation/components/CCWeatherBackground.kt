package com.example.weatherapp2026.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp2026.R
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCWeatherBackground(
    m_weatherType: CCWeatherType,
    m_modifier: Modifier = Modifier,
    m_useSunnyVariant: Boolean = false,
    m_content: @Composable () -> Unit
) {
    val m_imageRes = when (m_weatherType) {
        CCWeatherType.SUNNY -> if (m_useSunnyVariant) R.drawable.bg_sunny else R.drawable.bg_forest
        CCWeatherType.CLOUDY -> R.drawable.bg_cloudy
        CCWeatherType.RAINY -> R.drawable.bg_rainy
    }

    Box(modifier = m_modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = m_imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        m_content()
    }
}

@Preview(name = "Background — Sunny (Home / Forest)", showSystemUi = true)
@Composable
private fun CCWeatherBackgroundSunnyPreview() {
    CCWeatherTheme { CCWeatherBackground(CCWeatherType.SUNNY) {} }
}

@Preview(name = "Background — Sunny (Forecast / Sun)", showSystemUi = true)
@Composable
private fun CCWeatherBackgroundSunnyVariantPreview() {
    CCWeatherTheme { CCWeatherBackground(CCWeatherType.SUNNY, m_useSunnyVariant = true) {} }
}

@Preview(name = "Background — Cloudy", showSystemUi = true)
@Composable
private fun CCWeatherBackgroundCloudyPreview() {
    CCWeatherTheme { CCWeatherBackground(CCWeatherType.CLOUDY) {} }
}

@Preview(name = "Background — Rainy", showSystemUi = true)
@Composable
private fun CCWeatherBackgroundRainyPreview() {
    CCWeatherTheme { CCWeatherBackground(CCWeatherType.RAINY) {} }
}
