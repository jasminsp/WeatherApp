package com.jasminsp.weatherapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jasminsp.weatherapp.R
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

fun setGradient(color: Color): Brush {
    return Brush.verticalGradient(listOf(Color.Transparent, color),
        0F, 500F)
}

// Returns condition in string or the icon for it
fun getWeatherCondition(weatherCode: Int, returnOption: Int): Any {
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
        "Clear sky" -> R.drawable.sunny
        "Mainly clear" -> R.drawable.sunny
        "Partly cloudy" -> R.drawable.sunny
        "Overcast"  -> R.drawable.sunny
        "Fog" -> R.drawable.sunny
        "Drizzle" -> R.drawable.sunny
        "Rain" -> R.drawable.sunny
        "Freezing rain" -> R.drawable.sunny
        "Snow" -> R.drawable.sunny
        "Snow grains" -> R.drawable.sunny
        "Rain showers" -> R.drawable.sunny
        "Snow showers"  -> R.drawable.sunny
        "Thunderstorm" -> R.drawable.sunny
        "Thunderstorm with hail" -> R.drawable.sunny
        else -> {
            R.drawable.sunny
        }
    }

    val gradient = when (weatherCode) {
        0 -> setGradient(Color.Yellow)
        1 -> setGradient(Color.Yellow)
        2 -> setGradient(Color.White)
        3 -> setGradient(Color.Gray)
        in 45..48 -> setGradient(Color.Gray)
        in 51..55 -> setGradient(Color.Gray)
        in 61..65 -> setGradient(Color.Gray)
        in 65..66 -> setGradient(Color.Blue)
        in 71..75 -> setGradient(Color.Gray)
        77 -> setGradient(Color.Gray)
        in 80..82 -> setGradient(Color.Gray)
        in 85..86 -> setGradient(Color.Gray)
        95 -> setGradient(Color.Gray)
        in 96..99 -> setGradient(Color.Gray)
        else -> {setGradient(Color.Blue)}
    }

    val data = when (returnOption) {
        0 -> condition
        1 -> conditionIcon
        2 -> gradient
        else -> {condition}
    }
    return data
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

