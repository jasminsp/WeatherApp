package com.jasminsp.weatherapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.jasminsp.weatherapp.database.FavouriteData
import com.jasminsp.weatherapp.database.WeatherDatabase
import com.jasminsp.weatherapp.web.LocationApiService
import com.jasminsp.weatherapp.web.WeatherApiService

class WeatherRepository {
    private val call = WeatherApiService.service
    suspend fun getWeather(lat: Double, long: Double) = call.getHourlyWeatherWithLocation(lat, long)
}