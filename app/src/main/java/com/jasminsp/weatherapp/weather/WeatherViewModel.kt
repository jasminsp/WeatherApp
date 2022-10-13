package com.jasminsp.weatherapp.weather

import android.util.Log
import androidx.lifecycle.*
import com.jasminsp.weatherapp.database.FavouriteData
import com.jasminsp.weatherapp.repository.LocationRepository
import com.jasminsp.weatherapp.repository.WeatherRepository
import com.jasminsp.weatherapp.utils.errorToast
import com.jasminsp.weatherapp.web.LocationApiService
import com.jasminsp.weatherapp.web.WeatherApiService
import kotlinx.coroutines.*

class WeatherViewModel: ViewModel() {
    private val locationRepository = LocationRepository()
    private val weatherRepository = WeatherRepository
    val searchedLocations = MutableLiveData<LocationApiService.Model.Result>()
    val favouriteLocations = MutableLiveData<MutableSet<WeatherApiService.MainWeather>>()
    private var allFavourites = mutableSetOf<WeatherApiService.MainWeather>()
    var yourLocation = MutableLiveData<WeatherApiService.MainWeather>()

    // Get favourite data from database saved latitude and longitude
    var favouritesFromDb: LiveData<List<FavouriteData>> = weatherRepository.favouriteData

    // Locations from locationRepository by searched location name
    fun getLocations(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
                val serverResp = locationRepository.getLocations(name)
                searchedLocations.postValue(serverResp)
        }
    }

    // Combine weather data calls from two different apis
    fun getAllWeather() {
        var result: WeatherApiService.MainWeather? = null
        viewModelScope.launch(Dispatchers.IO) {
            favouritesFromDb.value?.forEach {
                val resp = async { weatherRepository.getWeather(it.latitude, it.longitude) }
                val resp2 = async { locationRepository.getLocationById(it.locationUid) }
                result = resp.await()
                result!!.id = resp2.await().id
                result!!.name = resp2.await().name
                allFavourites.add(result!!)
                delay(500)
                favouriteLocations.postValue(allFavourites)
            }
        }
    }

    fun getWeatherByLocation(lat: Double, long: Double) {
        Log.d("YourLocation", "lat: $lat, lon: $long")
        var result: WeatherApiService.MainWeather? = null
        viewModelScope.launch(Dispatchers.IO) {
            result = weatherRepository.getWeather(lat, long)
        }
        if (result != null) yourLocation.value = result!!
        Log.d("YourLocation", yourLocation.value.toString())
    }

    // add favourite longitude and latitude to database
    fun addFavourite(id: Int, lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.addFavourite(id, lat, long)
        }
    }

    // delete favourite from database
    fun deleteFavourite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteLocations.value?.removeIf { it.id == id }
            weatherRepository.deleteFavourite(id)
        }
    }
}