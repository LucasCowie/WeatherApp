package com.example.weatherapp.models
data class UpcomingWeather (
    val date: String,
    open val weatherType: String,
    val weatherIcon: Int,
    //Temp
    val high: Int,
    val feelsLike: Int,
    val low: Int,
    //rain
    val rainChance: Int,
    val rainAmount: Int,
    //wind
    val windSpeed: Int,
    val windDiceration: String,
    //humidity
    val humidity: Int
)