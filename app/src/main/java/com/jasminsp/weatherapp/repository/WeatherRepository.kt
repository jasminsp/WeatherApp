package com.jasminsp.weatherapp.repository

import com.jasminsp.weatherapp.web.WeatherApiService

class WeatherRepository {
    private val call = WeatherApiService.service
    suspend fun getWeatherData() = call.getWeather()
}