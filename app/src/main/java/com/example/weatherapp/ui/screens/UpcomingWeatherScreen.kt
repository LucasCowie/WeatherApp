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

@Composable
fun UpcomingWeatherScreen() {

    val forecast = listOf(
        UpcomingWeather(
            date = "Monday",
            weatherType = "Sunny",
            weatherIcon = R.drawable.avd_clear_day,
            high = 18,
            feelsLike = 17,
            low = 8,
            rainChance = 5,
            rainAmount = 0,
            windSpeed = 9,
            windDiceration = "W",
            humidity = 48
        ),
        UpcomingWeather(
            date = "Tuesday",
            weatherType = "Partly Cloudy",
            weatherIcon = R.drawable.avd_partly_cloudy,
            high = 15,
            feelsLike = 13,
            low = 6,
            rainChance = 30,
            rainAmount = 1,
            windSpeed = 14,
            windDiceration = "SW",
            humidity = 62
        ),
        UpcomingWeather(
            date = "Wednesday",
            weatherType = "Showers",
            weatherIcon = R.drawable.avd_showers,
            high = 12,
            feelsLike = 10,
            low = 5,
            rainChance = 80,
            rainAmount = 6,
            windSpeed = 20,
            windDiceration = "S",
            humidity = 85
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(forecast) { day ->
            UpcomingWeatherCard(day)
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun UpcomingWeatherCard(weather: UpcomingWeather) {
    val image = AnimatedImageVector.animatedVectorResource(weather.weatherIcon)
    var atEnd by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { atEnd = true }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAnimatedVectorPainter(image, atEnd),
                contentDescription = weather.weatherType,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = weather.date, style = MaterialTheme.typography.titleMedium)
                Text(text = weather.weatherType, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rain ${weather.rainChance}%  ·  Wind ${weather.windSpeed} km/h ${weather.windDiceration}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${weather.high}°", style = MaterialTheme.typography.headlineSmall)
                Text(text = "${weather.low}°", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
