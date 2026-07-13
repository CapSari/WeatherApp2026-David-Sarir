package com.example.weatherapp2026.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp2026.presentation.theme.CCColorBackground
import com.example.weatherapp2026.presentation.theme.CCColorPrimary
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCLoadingView(m_modifier: Modifier = Modifier) {
    Box(
        modifier = m_modifier
            .fillMaxSize()
            .background(CCColorBackground),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = CCColorPrimary)
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Loading View")
@Composable
private fun CCLoadingViewPreview() {
    CCWeatherTheme {
        CCLoadingView()
    }
}
