package com.jasminsp.weatherapp.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jasminsp.weatherapp.database.FavouriteData
import com.jasminsp.weatherapp.repository.LocationRepository
import com.jasminsp.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application): AndroidViewModel(application) {
    private val locationRepository = LocationRepository()
    private val weatherRepository = WeatherRepository()

    fun getLocations(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val serverResp = locationRepository.getLocations(name)
            Log.i("RESPONSE", "$serverResp")
        }
    }

    fun getFavouriteWeather(lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val serverResp = weatherRepository.getWeather(lat, long)
            Log.i("RESPONSE", "$serverResp")
        }
    }
}