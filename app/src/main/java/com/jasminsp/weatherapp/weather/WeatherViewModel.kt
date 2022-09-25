package com.jasminsp.weatherapp.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasminsp.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application): AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository()
    val weatherData = MutableLiveData<Int>()

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val serverResp = weatherRepository.getWeatherData()
            Log.i("RESPONSE", "$serverResp")
        }
    }
}