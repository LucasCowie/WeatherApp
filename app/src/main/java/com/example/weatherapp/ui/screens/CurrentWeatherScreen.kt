package com.example.weatherapp.ui.screens

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.weatherapp.data.Current
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.data.icon
import com.example.weatherapp.ui.BloodCard
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherScreen(weather: WeatherResponse) {
    val current = weather.current
    val today = weather.forecast.days.firstOrNull()?.day // current conditions have no chance-of-precip, so use today's

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        MainWeatherCard(current)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard("Precip chance", "${today?.chanceOfPrecip ?: 0}%", Modifier.weight(1f))
            InfoCard("Precip amount", "${current.precipMm} mm", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard("Wind", "${current.windKph.roundToInt()} km/h ${current.windDir}", Modifier.weight(1f))
            InfoCard("Humidity", "${current.humidity}%", Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MainWeatherCard(current: Current) {
    val image = AnimatedImageVector.animatedVectorResource(current.condition.icon(isDay = current.isDay == 1))
    var atEnd by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { atEnd = true }

    BloodCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAnimatedVectorPainter(image, atEnd),
                contentDescription = current.condition.text,
                modifier = Modifier.size(112.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = current.condition.text,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${current.tempC.roundToInt()}°",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary // ichor gold
            )
            Text(
                text = "Feels like ${current.feelsLikeC.roundToInt()}°",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    BloodCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
