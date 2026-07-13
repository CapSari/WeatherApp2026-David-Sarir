package com.example.weatherapp2026.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.weatherapp2026.presentation.theme.CCColorBackground
import com.example.weatherapp2026.presentation.theme.CCColorPrimary
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCErrorView(
    m_message: String,
    m_modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = m_modifier
            .fillMaxSize()
            .background(CCColorBackground)
            .padding(CCTheme.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.lg))

        Text(
            text = m_message,
            style = CCTheme.textStyles.bodyDetail,
            color = CCTheme.colors.onWeatherSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.xl))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = CCColorPrimary)
        ) {
            Text(text = "Retry", color = CCTheme.colors.onWeatherSurface)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Error — no network")
@Composable
private fun CCErrorViewPreview() {
    CCWeatherTheme {
        CCErrorView(
            m_message = "No internet connection and no cached data available.",
            onRetry = {}
        )
    }
}
