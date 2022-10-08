package com.jasminsp.weatherapp.repository

import com.jasminsp.weatherapp.web.LocationApiService

class LocationRepository {
    // Call api service for fetching locations for search
    private val call = LocationApiService.service
    suspend fun getLocations(name: String) = call.getLocationsByName(name)

    suspend fun getLocationById(id: Int) = call.getLocationById(id)
}