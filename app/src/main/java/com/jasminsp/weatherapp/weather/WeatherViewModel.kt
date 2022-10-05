package com.jasminsp.weatherapp.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.jasminsp.weatherapp.database.FavouriteData
import com.jasminsp.weatherapp.repository.LocationRepository
import com.jasminsp.weatherapp.repository.WeatherRepository
import com.jasminsp.weatherapp.utils.errorToast
import com.jasminsp.weatherapp.web.LocationApiService
import com.jasminsp.weatherapp.web.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application): AndroidViewModel(application) {
    private val locationRepository = LocationRepository()
    private val weatherRepository = WeatherRepository(application)
    val searchedLocations = MutableLiveData<LocationApiService.Model.Result>()
    val favouriteLocations = MutableLiveData<MutableSet<WeatherApiService.MainWeather>>()
    // Used to store favourite data temporarily before sending to favouriteLocations
    val allFavourites = mutableSetOf<WeatherApiService.MainWeather>()

    // Get favourite data from database saved latitude and longitude
    fun getFavourites(): LiveData<List<FavouriteData>> = weatherRepository.getFavouriteData()


    // Fetch location from LocationRepository by searched location name
    fun getLocations(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val serverResp = locationRepository.getLocations(name)
                Log.i("LOCATION_RESPONSE", "$serverResp")

                // Save data to variable as livedata
                searchedLocations.postValue(serverResp)
            } catch (error: Error) {
                errorToast(error)
            }
        }
    }

    // Fetch new weather data from weatherRepository by latitude and longitude from api
    fun getFavouriteWeather(lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = weatherRepository.getWeather(lat, long)
                Log.i("WEATHER_RESPONSE", "$resp")

                allFavourites.add(resp)
            } catch (error: Error) {
                errorToast(error)
            } finally {
                delay(2000)
                favouriteLocations.postValue(allFavourites)
            }
        }
    }

    // add favourite longitude and latitude to database
    fun addFavourite(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                weatherRepository.addFavourite(lat, long)
                delay(1000)
                getFavouriteWeather(lat, long)
            } catch (error: Error) {
                errorToast(error)
            }
        }
    }

    fun deleteFavourite(lat: Double, long: Double) {
        try {
            weatherRepository.deleteFavourite(lat, long)
        } catch (error: Error) {
            errorToast(error)
        }
    }
}