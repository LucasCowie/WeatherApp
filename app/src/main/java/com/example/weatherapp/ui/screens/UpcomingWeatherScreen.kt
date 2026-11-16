package com.example.weatherapp.ui.screens

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.ForecastDay
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.data.displayDate
import com.example.weatherapp.data.icon
import com.example.weatherapp.ui.BloodCard
import kotlin.math.roundToInt

@Composable
fun UpcomingWeatherScreen(weather: WeatherResponse) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(weather.forecast.days) { forecastDay ->
            UpcomingWeatherCard(forecastDay)
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun UpcomingWeatherCard(forecastDay: ForecastDay) {
    val day = forecastDay.day
    val image = AnimatedImageVector.animatedVectorResource(day.condition.icon())
    var atEnd by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { atEnd = true }

    BloodCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAnimatedVectorPainter(image, atEnd),
                contentDescription = day.condition.text,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = forecastDay.displayDate, style = MaterialTheme.typography.titleMedium)
                Text(text = day.condition.text, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Precip ${day.chanceOfPrecip}%  ·  ${day.totalPrecipMm} mm",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Wind up to ${day.maxWindKph.roundToInt()} km/h  ·  Humidity ${day.avgHumidity.roundToInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${day.maxTempC.roundToInt()}°", style = MaterialTheme.typography.headlineSmall)
                Text(text = "${day.minTempC.roundToInt()}°", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
