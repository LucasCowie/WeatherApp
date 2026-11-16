package com.example.weatherapp.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherResponse
import com.example.weatherapp.data.getDeviceLocation
import com.example.weatherapp.ui.screens.CurrentWeatherScreen
import com.example.weatherapp.ui.screens.LocationsScreen
import com.example.weatherapp.ui.screens.UpcomingWeatherScreen
import com.example.weatherapp.ui.theme.ClotBlack
import com.example.weatherapp.ui.theme.DriedBlood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(viewModel: MainViewModel = viewModel()) {

    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) } // default set to 0
    val context = LocalContext.current
    val weather = viewModel.weather

    // Shows the system "Allow location?" dialog, then loads weather for the device's location.
    // If we can't get a location, we don't guess a city: the user is asked to choose one instead.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.values.any { it }) {
            getDeviceLocation(context) { latLon ->
                if (latLon != null) viewModel.loadWeather(latLon, isDeviceLocation = true)
                else viewModel.locationError = "Couldn't find this device's location."
            }
        } else {
            viewModel.locationError = "Location permission was not allowed."
        }
    }

    fun loadDeviceLocation() {
        viewModel.locationError = null // back to the spinner while we try
        permissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    fun openLocations() = navController.navigate("locations") { launchSingleTop = true }

    // Show the city the user picked last time, otherwise the device's location
    fun loadStartingLocation() {
        val saved = viewModel.savedLocation
        if (saved != null) viewModel.loadWeather(saved) else loadDeviceLocation()
    }

    // On app start (skipped on screen rotation, when the ViewModel already has data)
    LaunchedEffect(Unit) {
        if (weather == null) loadStartingLocation()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    // The selected location, e.g. "Halifax, Nova Scotia"
                    Text(
                        when {
                            viewModel.locationError != null -> "Location unavailable"
                            weather != null -> "${weather.location.name}, ${weather.location.region}"
                            viewModel.error != null -> "Weather unavailable"
                            else -> "Loading…"
                        }
                    )
                },
                actions = {
                    // Opens the Locations screen to switch location
                    IconButton(onClick = ::openLocations) {
                        Icon(
                            painter = painterResource(R.drawable.ic_location),
                            contentDescription = "Change location",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                windowInsets = NavigationBarDefaults.windowInsets)
            {
                // 0. Current Weather
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(R.drawable.ic_daily_calendar), contentDescription = "", modifier = Modifier.scale(1.5f))},
                    label = { Text("Current Weather") },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate(route="current"){
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                // 1. Daily Forecast
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(R.drawable.ic_calendar), contentDescription = "",modifier = Modifier.scale(1.5f))},
                    label = { Text("Daily Forecast") },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate(route="upcoming"){
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(DriedBlood, ClotBlack))) // dark red fading to black
        ) {
            NavHost(
                navController = navController,
                startDestination = "current",
                modifier = Modifier.padding(top = 16.dp) // room for the drips
            ) {
                composable(route = "current") {
                    WeatherOrStatus(viewModel, ::loadStartingLocation, ::openLocations) { CurrentWeatherScreen(it) }
                }
                composable(route = "upcoming") {
                    WeatherOrStatus(viewModel, ::loadStartingLocation, ::openLocations) { UpcomingWeatherScreen(it) }
                }
                composable(route = "locations") {
                    LocationsScreen(
                        viewModel = viewModel,
                        onMyLocation = {
                            viewModel.saveLocation(null) // forget the picked city
                            loadDeviceLocation()
                            navController.popBackStack()
                        },
                        onCitySelected = { city ->
                            viewModel.saveLocation(city) // remember it for next time
                            viewModel.loadWeather(city)
                            navController.popBackStack()
                        }
                    )
                }
            }
            BloodDrips() // drawn on top, hanging from the top bar
        }
    }

}

// Shows the screen once weather has loaded. Until then a spinner. If the device's location or the
// weather request failed, shows what went wrong and asks the user to choose a location or try again.
@Composable
private fun WeatherOrStatus(
    viewModel: MainViewModel,
    onRetry: () -> Unit,
    onChooseLocation: () -> Unit,
    screen: @Composable (WeatherResponse) -> Unit
) {
    val weather = viewModel.weather
    val problem = viewModel.locationError ?: viewModel.error
    if (weather != null && viewModel.locationError == null) {
        screen(weather)
        return
    }
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        if (problem == null) {
            CircularProgressIndicator()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(problem)
                Text("Choose a location yourself, or try again.")
                Button(onClick = onChooseLocation) { Text("Choose a location") }
                OutlinedButton(onClick = onRetry) { Text("Try again") }
            }
        }
    }
}
