package com.jasminsp.weatherapp.utils.helpers

import androidx.compose.ui.graphics.Color
import com.jasminsp.weatherapp.R
import com.jasminsp.weatherapp.web.WeatherApiService
import kotlin.math.roundToInt

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

    val gradientColor = when (weatherCode) {
        0 -> Color.Yellow
        1 -> Color.Blue
        2 -> Color.Yellow
        3 -> Color.Gray
        in 45..48 -> Color.Yellow
        in 51..55 -> Color.Yellow
        in 61..65 -> Color.LightGray
        in 65..66 -> Color.Yellow
        in 71..75 -> Color.Yellow
        77 -> Color.Yellow
        in 80..82 -> Color.Yellow
        in 85..86 -> Color.Yellow
        95 -> Color.Yellow
        in 96..99 -> Color.Yellow
        else -> {
            Color.Yellow}
    }

    val data = when (returnOption) {
        0 -> condition
        1 -> conditionIcon
        2 -> gradientColor
        else -> {condition}
    }
    return data
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

fun getCurrentTemperature(favourite: WeatherApiService.MainWeather): Int {
    return favourite.current_weather.temperature.toInt()
}

fun getHourlyWeather(favourite: WeatherApiService.MainWeather): List<Pair<String, Double>> {
    return favourite.hourly.time.zip(favourite.hourly.temperature_2m)
}
