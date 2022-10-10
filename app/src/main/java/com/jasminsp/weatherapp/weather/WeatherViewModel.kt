package com.jasminsp.weatherapp.weather

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

    // Get favourite data from database saved latitude and longitude
    var favouritesFromDb: LiveData<List<FavouriteData>> = weatherRepository.favouriteData

    // Fetch location from LocationRepository by searched location name
    fun getLocations(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
                val serverResp = locationRepository.getLocations(name)
                // Save data to variable as livedata
                searchedLocations.postValue(serverResp)
        }
    }

    // Get location name by lat and long
    private suspend fun getLocationNameById(id: Int): String = withContext(Dispatchers.IO) {
        return@withContext locationRepository.getLocationById(id).name
    }

    // Fetch new weather data from weatherRepository by latitude and longitude from api
    fun getFavouriteWeather(lat: Double, long: Double, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = async { weatherRepository.getWeather(lat, long) }
                allFavourites.add(resp.await())
                allFavourites.last().id = id

                val name = async { getLocationNameById(id) }
                allFavourites.last().name = name.await()
            } catch (error: Error) {
                errorToast(error)
            } finally {
                favouriteLocations.postValue(allFavourites)
            }
        }
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