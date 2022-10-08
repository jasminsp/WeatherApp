package com.jasminsp.weatherapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import com.jasminsp.weatherapp.weather.WeatherViewModel
import com.jasminsp.weatherapp.web.WeatherApiService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, id: Int, lat: Double, long: Double) {
    viewModel.addFavourite(id, lat, long)
}

fun formatDate(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateFormatter.format(date)
}

fun getDateToday(): String {
    return formatDate(LocalDateTime.now())
}

fun isDateToday(date: String): Boolean {
    if(date == getDateToday()) {
        return true
    }
    return false
}

// Min/Max for the main view card
fun getMinMaxTempToday(favourite: WeatherApiService.MainWeather, returnMin: Boolean = false): Int? {
    var minTempToday: Double? = null
    var maxTempToday: Double? = null

    favourite.daily.time.forEach { time ->
        if (isDateToday(time)) {
            val timeIndex = favourite.daily.time.indexOf(time)
            minTempToday = favourite.daily.temperature_2m_min[timeIndex]
            maxTempToday = favourite.daily.temperature_2m_max[timeIndex]
        }
    }
    return if (returnMin) minTempToday?.roundToInt() else maxTempToday?.roundToInt()
}

// Returns condition in string or the icon for it
fun getWeatherCondition(weatherCode: Int, returnIcon: Boolean = false): Any {
    val condition = when (weatherCode) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        in 45..48 -> "Fog"
        in 51..55 -> "Drizzle"
        in 61..65 -> "Rain"
        in 65..66 -> "Freezing rain"
        in 71..75 -> "Snow"
        77 -> "Snow grains"
        in 80..82 -> "Rain showers"
        in 85..86 -> "Snow showers"
        95 -> "Thunderstorm"
        in 96..99 -> "Thunderstorm with hail"
        else -> {"Clear sky"}
    }
    // Change these mock icons to real icons
    val conditionIcon = when (condition) {
        "Clear sky" -> Icons.Default.Star
        "Mainly clear" -> Icons.Default.Star
        "Partly cloudy" -> Icons.Default.Star
        "Overcast"  -> Icons.Default.Star
        "Fog" -> Icons.Default.Star
        "Drizzle" -> Icons.Default.Star
        "Rain" -> Icons.Default.Star
        "Freezing rain" -> Icons.Default.Star
        "Snow" -> Icons.Default.Star
        "Snow grains" -> Icons.Default.Star
        "Rain showers" -> Icons.Default.Star
        "Snow showers"  -> Icons.Default.Star
        "Thunderstorm" -> Icons.Default.Star
        "Thunderstorm with hail" -> Icons.Default.Star
        else -> {
            Icons.Default.Star
        }
    }
    return if (returnIcon) conditionIcon else condition
}

fun getCurrentTemperature(favourite: WeatherApiService.MainWeather): Int {
    return favourite.current_weather.temperature.toInt()
}

fun getHourlyWeather(favourite: WeatherApiService.MainWeather): List<Pair<String, Double>> {
    return favourite.hourly.time.zip(favourite.hourly.temperature_2m)
}

// All daily weather variables/ use case: getDailyWeatherVariables(favourite, 4)
fun getDailyWeatherVariables(favourite: WeatherApiService.MainWeather, returnOption: Int): List<Pair<String, Any>> {
    val dailyTempMin = favourite.daily.time.zip(favourite.daily.temperature_2m_min)
    val dailyTempMax = favourite.daily.time.zip(favourite.daily.temperature_2m_max)
    val dailyTempMinApparent = favourite.daily.time.zip(favourite.daily.apparent_temperature_min)
    val dailyTempMaxApparent = favourite.daily.time.zip(favourite.daily.apparent_temperature_max)
    val sunrise = favourite.daily.time.zip(favourite.daily.sunrise)
    val sunset = favourite.daily.time.zip(favourite.daily.sunset)

    val data = when (returnOption) {
        0 -> dailyTempMin
        1 -> dailyTempMax
        2 -> dailyTempMinApparent
        3 -> dailyTempMaxApparent
        4 -> sunrise
        5 -> sunset
        else -> {dailyTempMin}
    }
    return data
}

