package com.example.weatherapp.data

import com.example.weatherapp.R
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// These classes mirror the JSON returned by WeatherAPI's forecast.json, so Gson can fill them in.
data class WeatherResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String
)

data class Condition(
    val text: String,
    val code: Int
)

data class Current(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("precip_mm") val precipMm: Double,
    val humidity: Int,
    @SerializedName("is_day") val isDay: Int, // 1 during the day, 0 at night
    val condition: Condition
)

data class Forecast(
    @SerializedName("forecastday") val days: List<ForecastDay>
)

data class ForecastDay(
    val date: String, // "2026-11-16"
    val day: Day
)

data class Day(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("maxwind_kph") val maxWindKph: Double,
    @SerializedName("totalprecip_mm") val totalPrecipMm: Double,
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int,
    @SerializedName("daily_chance_of_snow") val chanceOfSnow: Int,
    @SerializedName("avghumidity") val avgHumidity: Double,
    val condition: Condition
) {
    // "Any probability of precipitation": rain or snow, whichever is higher
    val chanceOfPrecip: Int get() = maxOf(chanceOfRain, chanceOfSnow)
}

// One city returned by WeatherAPI's search.json
data class SearchResult(
    val id: Long,
    val name: String,
    val region: String,
    val country: String
)

// Picks one of our animated icons from WeatherAPI's condition code
// (codes: https://www.weatherapi.com/docs/weather_conditions.json)
fun Condition.icon(isDay: Boolean = true): Int = when (code) {
    1000 -> if (isDay) R.drawable.avd_clear_day else R.drawable.avd_clear_night
    1003 -> if (isDay) R.drawable.avd_partly_cloudy else R.drawable.avd_partly_cloudy_night
    1006, 1009 -> R.drawable.avd_cloudy                                   // cloudy, overcast
    1030, 1135, 1147 -> R.drawable.avd_fog                                // mist, fog
    1087, 1273, 1276, 1279, 1282 -> R.drawable.avd_thunderstorm
    1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258 -> R.drawable.avd_snow
    else -> R.drawable.avd_showers                                        // rain, drizzle, sleet, ice pellets
}

// "2026-11-16" -> "Monday, Nov 16"
val ForecastDay.displayDate: String
    get() = LocalDate.parse(date).format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
