package com.jasminsp.weatherapp.location

import android.location.Location
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    var userLatitude = 0.0
    var userLongtitude = 0.0

    fun updateLocation(newLocation: Location) {
        userLatitude = newLocation.latitude
        userLongtitude = newLocation.longitude
    }
}