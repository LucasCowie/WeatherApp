package com.example.weatherapp.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // e.g. v1/forecast.json?key=...&q=44.65,-63.57&days=3
    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") key: String,
        @Query("q") location: String,  // "lat,lon", a city name, or "id:123" from a search result
        @Query("days") days: Int
    ): WeatherResponse

    // e.g. v1/search.json?key=...&q=Hali  -> cities whose name starts with "Hali"
    @GET("v1/search.json")
    suspend fun searchCities(
        @Query("key") key: String,
        @Query("q") query: String
    ): List<SearchResult>
}
