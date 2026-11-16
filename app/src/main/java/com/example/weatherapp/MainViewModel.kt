package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.SearchResult
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

// AndroidViewModel (instead of ViewModel) gives us the Application, which we need for SharedPreferences
class MainViewModel(application: Application) : AndroidViewModel(application) {
    // A small key-value file on the device; it survives closing the app and rebooting the phone
    private val prefs = application.getSharedPreferences("weather", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Read straight from the repository (single source of truth)
    val weather: WeatherResponse?
        get() = WeatherRepository.weather

    val error: String?
        get() = WeatherRepository.error

    val searchResults: List<SearchResult>
        get() = WeatherRepository.searchResults

    val searchError: String?
        get() = WeatherRepository.searchError

    // The city the user picked from search, e.g. "id:314643". null means "use my device's location".
    val savedLocation: String?
        get() = prefs.getString("location", null)

    // Why we couldn't get the device's location (permission denied, location off...), or null if we could.
    // While this is set, the screens ask the user to choose a location instead.
    var locationError by mutableStateOf<String?>(null)

    // Name of the place the device is in (e.g. "Halifax"), known once we've loaded weather for it
    var myLocationName by mutableStateOf<String?>(null)
        private set

    // The user's "Saved Locations" list. Stored in prefs as JSON text, e.g. [{"id":314643,"name":"Halifax",...}]
    var savedLocations by mutableStateOf(
        gson.fromJson(prefs.getString("savedLocations", "[]"), Array<SearchResult>::class.java).toList()
    )
        private set

    fun loadWeather(location: String, isDeviceLocation: Boolean = false) {
        locationError = null // we have a location now
        viewModelScope.launch {
            WeatherRepository.fetchWeather(location)
            if (isDeviceLocation) myLocationName = weather?.location?.name
        }
    }

    // Called once we know the device's location ("lat,lon"), or with null and the reason if we couldn't get it.
    // showWeather = true: show that location's weather. false: only look up its name for "My Location".
    fun onDeviceLocation(latLon: String?, showWeather: Boolean, problem: String) {
        when {
            latLon == null -> if (showWeather) locationError = problem
            showWeather -> loadWeather(latLon, isDeviceLocation = true)
            else -> viewModelScope.launch { myLocationName = WeatherRepository.findLocationName(latLon) }
        }
    }

    // Remember the user's choice for next time. Pass null to go back to the device's location.
    fun saveLocation(location: String?) {
        prefs.edit { putString("location", location) }
    }

    fun searchCities(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            WeatherRepository.searchCities(query.trim())
        }
    }

    fun addSavedLocation(city: SearchResult) {
        if (city !in savedLocations) updateSavedLocations(savedLocations + city)
    }

    fun removeSavedLocation(city: SearchResult) {
        updateSavedLocations(savedLocations - city)
    }

    private fun updateSavedLocations(cities: List<SearchResult>) {
        savedLocations = cities
        prefs.edit { putString("savedLocations", gson.toJson(cities)) }
    }
}
