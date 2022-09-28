package com.jasminsp.weatherapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.jasminsp.weatherapp.database.FavouriteData
import com.jasminsp.weatherapp.database.WeatherDatabase
import com.jasminsp.weatherapp.web.WeatherApiService

class WeatherRepository(application: Application) {
    private val weatherDao = WeatherDatabase.get(application.applicationContext).weatherDatabaseDao()
    private val call = WeatherApiService.service

    val favouriteData: LiveData<List<FavouriteData>> = weatherDao.getAllFavourites()

    // Get weather data from api
    suspend fun getWeather(lat: Double, long: Double) = call.getHourlyWeatherWithLocation(lat, long)

    // Add data favourite to database
    suspend fun addFavourite(lat: Double, long: Double) {
        weatherDao.insertOrUpdate(FavouriteData(0, lat, long))
    }
}