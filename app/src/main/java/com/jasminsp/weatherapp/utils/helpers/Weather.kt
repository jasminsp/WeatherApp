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
        "Clear sky" -> R.drawable.clear_icon
        "Mainly clear" -> R.drawable.clear_icon
        "Partly cloudy" -> R.drawable.cloudy_icon
        "Overcast"  -> R.drawable.cloudy_icon
        "Fog" -> R.drawable.cloudy_icon
        "Drizzle" -> R.drawable.rainy_icon
        "Rain" -> R.drawable.rainy_icon
        "Freezing rain" -> R.drawable.rainy_icon
        "Snow" -> R.drawable.snowy_icon
        "Snow grains" -> R.drawable.snowy_icon
        "Rain showers" -> R.drawable.rainy_icon
        "Snow showers"  -> R.drawable.snowy_icon
        "Thunderstorm" -> R.drawable.stormy_icon
        "Thunderstorm with hail" -> R.drawable.stormy_icon
        else -> {
            R.drawable.clear_icon
        }
    }

    val backgroundImage = when (condition) {
        "Clear sky" -> R.mipmap.clear_illu2
        "Mainly clear" -> R.mipmap.misty_illu
        "Partly cloudy" -> R.mipmap.cloudy_illu2
        "Overcast"  -> R.mipmap.cloudy_illu
        "Fog" -> R.mipmap.foggy_illu
        "Drizzle" -> R.mipmap.rainy_illu
        "Rain" -> R.mipmap.rainy_illu
        "Freezing rain" -> R.drawable.rainy_icon
        "Snow" -> R.mipmap.snowy_illu
        "Snow grains" -> R.mipmap.snowy_illu
        "Rain showers" -> R.mipmap.rainy_illu
        "Snow showers"  -> R.mipmap.snowy_illu
        "Thunderstorm" -> R.mipmap.windy_illu
        "Thunderstorm with hail" -> R.mipmap.stormy_illu
        else -> {
            R.mipmap.clear_illu
        }
    }

    val data = when (returnOption) {
        0 -> condition
        1 -> conditionIcon
        2 -> backgroundImage
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
