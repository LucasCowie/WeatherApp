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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.weatherapp.R
import com.example.weatherapp.models.UpcomingWeather
import androidx.compose.foundation.layout.RowScope

@Composable
fun CurrentWeatherScreen() {
    val weather = UpcomingWeather(
        weatherIcon = R.drawable.avd_clear_day,
        date = "Sunday",
        weatherType = "Sunny",
        high = 16,
        feelsLike = 14,
        low = 7,
        rainChance = 10,
        rainAmount = 0,
        windDiceration = "NW",
        humidity = 55,
        windSpeed = 11
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainWeatherCard(weather)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard("Rain chance", "${weather.rainChance}%", Modifier.weight(1f))
            InfoCard("Rain amount", "${weather.rainAmount} mm", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard(
                "Wind",
                "${weather.windSpeed} km/h ${weather.windDiceration}",
                Modifier.weight(1f)
            )
            InfoCard("Humidity", "${weather.humidity}%", Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun MainWeatherCard(weather: UpcomingWeather) {
    val image = AnimatedImageVector.animatedVectorResource(weather.weatherIcon)
    var atEnd by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { atEnd = true }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.date,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Image(
                painter = rememberAnimatedVectorPainter(image, atEnd),
                contentDescription = weather.weatherType,
                modifier = Modifier.size(112.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = weather.weatherType,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${weather.high}°",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Low ${weather.low}°  ·  Feels like ${weather.feelsLike}°",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
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