package com.jasminsp.weatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jasminsp.weatherapp.database.FavouriteData
import com.jasminsp.weatherapp.database.WeatherDatabase
import com.jasminsp.weatherapp.weather.WeatherViewModel
import com.jasminsp.weatherapp.web.WeatherApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object WeatherRepository {
    private val weatherDao = WeatherDatabase.get().weatherDatabaseDao()
    private val call = WeatherApiService.service
    val favouriteData: LiveData<List<FavouriteData>> = weatherDao.getAllFavourites()


    // Get weather data from api
    suspend fun getWeather(lat: Double, long: Double) = call.getHourlyWeatherWithLocation(lat, long)

    // Add data favourite to database
    suspend fun addFavourite(id: Int, lat: Double, long: Double) {
        weatherDao.insertOrUpdate(FavouriteData(id, lat, long))
    }

    suspend fun deleteFavourite(id: Int) {
        weatherDao.deleteByLatLong(id)
    }

    // Get updated weather data for favourites with worker
    fun refreshFavourites() {
        val viewModel = WeatherViewModel()
        CoroutineScope(Dispatchers.IO).launch {
            favouriteData.value?.forEach {
                viewModel.getFavouriteWeather(it.latitude, it.longitude, it.locationUid)
                Log.i("WORKER", "refressing ${it.locationUid}")
            }
        }
    }
}