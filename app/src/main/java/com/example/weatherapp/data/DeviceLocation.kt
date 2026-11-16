package com.example.weatherapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

// Asks the device for one fresh location fix. onResult gets "lat,lon" for WeatherAPI, or null if
// location is turned off or no fix arrives.
@SuppressLint("MissingPermission") // only called after the user has granted location permission
fun getDeviceLocation(context: Context, onResult: (String?) -> Unit) {
    val locationManager = context.getSystemService(LocationManager::class.java)
    // Network (wifi/cell) is faster; fall back to GPS (what the emulator has)
    val provider =
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) LocationManager.NETWORK_PROVIDER
        else LocationManager.GPS_PROVIDER

    LocationManagerCompat.getCurrentLocation(
        locationManager,
        provider,
        CancellationSignal(),
        ContextCompat.getMainExecutor(context)
    ) { location ->
        onResult(location?.let { "${it.latitude},${it.longitude}" })
    }
}
