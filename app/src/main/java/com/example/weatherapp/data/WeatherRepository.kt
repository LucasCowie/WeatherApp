package com.example.weatherapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.weatherapp.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/") // Android blocks plain http by default
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    var weather by mutableStateOf<WeatherResponse?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var searchResults by mutableStateOf<List<SearchResult>>(emptyList())
        private set

    var searchError by mutableStateOf<String?>(null)
        private set

    suspend fun fetchWeather(location: String) {
        weather = null // clear the old location's weather so the spinner (or an error) shows
        error = null
        try {
            weather = withNetworkTimeout {
                api.getForecast(BuildConfig.WEATHER_API_KEY, location, days = 3)
            }
        } catch (e: Exception) {
            rethrowIfCancelled(e)
            error = "Couldn't load weather: ${e.message}" // no internet, bad API key, timeout, etc.
        }
    }

    suspend fun searchCities(query: String) {
        searchError = null
        try {
            searchResults = withNetworkTimeout {
                api.searchCities(BuildConfig.WEATHER_API_KEY, query)
            }
            if (searchResults.isEmpty()) searchError = "No cities found for \"$query\""
        } catch (e: Exception) {
            rethrowIfCancelled(e)
            searchResults = emptyList()
            searchError = "Search failed: ${e.message}"
        }
    }

    // Give up after 15s so a bad connection shows an error instead of spinning forever
    private suspend fun <T> withNetworkTimeout(request: suspend () -> T): T =
        withTimeout(15_000) { request() }

    // Let real coroutine cancellation through (but not our own timeout, which is an error to show)
    private fun rethrowIfCancelled(e: Exception) {
        if (e is CancellationException && e !is TimeoutCancellationException) throw e
    }
}
