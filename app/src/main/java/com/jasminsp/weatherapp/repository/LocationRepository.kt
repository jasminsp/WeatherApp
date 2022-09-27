package com.jasminsp.weatherapp.repository

import com.jasminsp.weatherapp.web.LocationApiService

class LocationRepository {
    private val call = LocationApiService.service
    suspend fun getLocations(name: String) = call.getLocationsByName(name)
}