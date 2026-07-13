package com.example.weatherapp2026.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun CCCurrentWeatherHeader(
    m_weather: CCCurrentWeather,
    m_modifier: Modifier = Modifier
) {
    val m_dateLabel = remember { formattedTodayDate() }

    Column(
        modifier = m_modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CCWeatherIcon(
            m_iconCode = m_weather.m_iconCode,
            m_contentDescription = m_weather.m_weatherCondition,
            m_tint = CCTheme.colors.onWeatherSurface,
            m_modifier = Modifier.size(CCTheme.spacing.iconHeader)
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.lg))

        Text(
            text = "${m_weather.m_temperature.roundToInt()}°",
            style = CCTheme.textStyles.temperatureDisplay
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.xs))

        Text(
            text = m_weather.m_weatherDescription,
            style = CCTheme.textStyles.weatherDescription
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.sm))

        Text(
            text = m_weather.m_cityName,
            style = CCTheme.textStyles.cityName
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.sm))

        Text(
            text = m_dateLabel,
            style = CCTheme.textStyles.dateLabel
        )

        Spacer(modifier = Modifier.height(CCTheme.spacing.sm))

        Text(
            text = "Feels like ${m_weather.m_feelsLike.roundToInt()}°  •  Humidity ${m_weather.m_humidity}%",
            style = CCTheme.textStyles.bodyDetail
        )
    }
}

private fun formattedTodayDate(): String {
    val m_cal = Calendar.getInstance()
    val m_day = m_cal.get(Calendar.DAY_OF_MONTH)
    val m_ordinal = when {
        m_day in 11..13 -> "th"
        m_day % 10 == 1 -> "st"
        m_day % 10 == 2 -> "nd"
        m_day % 10 == 3 -> "rd"
        else -> "th"
    }
    val m_dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(m_cal.time)
    val m_month = SimpleDateFormat("MMMM", Locale.getDefault()).format(m_cal.time)
    val m_year = m_cal.get(Calendar.YEAR)
    return "$m_dayName, $m_day$m_ordinal $m_month $m_year"
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

private fun previewWeather(
    m_iconCode: String = "01d",
    m_conditionId: Int = 800,
    m_description: String = "Clear sky",
    m_city: String = "Johannesburg"
) = CCCurrentWeather(
    m_cityName = m_city,
    m_temperature = 25.0,
    m_feelsLike = 24.0,
    m_humidity = 45,
    m_weatherCondition = "Clear",
    m_weatherDescription = m_description,
    m_iconCode = m_iconCode,
    m_windSpeed = 3.5,
    m_conditionId = m_conditionId,
    m_timestamp = 0L
)

@Preview(name = "Header — Sunny", showBackground = true, backgroundColor = 0xFF47AB2F)
@Composable
private fun CCCurrentWeatherHeaderSunnyPreview() {
    CCWeatherTheme {
        CCCurrentWeatherHeader(m_weather = previewWeather("01d", 800, "Clear sky"))
    }
}

@Preview(name = "Header — Cloudy", showBackground = true, backgroundColor = 0xFF54717A)
@Composable
private fun CCCurrentWeatherHeaderCloudyPreview() {
    CCWeatherTheme {
        CCCurrentWeatherHeader(m_weather = previewWeather("04d", 804, "Overcast clouds", "Cape Town"))
    }
}

@Preview(name = "Header — Rainy", showBackground = true, backgroundColor = 0xFF57575D)
@Composable
private fun CCCurrentWeatherHeaderRainyPreview() {
    CCWeatherTheme {
        CCCurrentWeatherHeader(m_weather = previewWeather("10d", 501, "Moderate rain", "Durban"))
    }
}
