package com.jasminsp.weatherapp.web

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object LocationApiService {
    // API base url
    private const val URL = "https://geocoding-api.open-meteo.com/v1/"

    // data classes to hold the search result
    object Model {
        data class Result(val results: Array<Location>)
        data class Location(
            val id: Int,
            val name: String,
            val latitude: Double,
            val longitude: Double,
            val country_code: String?,
            val timezone: String?,
            val country: String?,
            val admin1: String?,
            val admin2: String?,
            val admin3: String?
        )
    }

    // Get request to endpoint
    interface Service {
        @GET("search?")
        suspend fun getLocationsByName(@Query("name") action: String): Model.Result

        @GET("get?")
        suspend fun getLocationById(@Query("id") action: Int): Model.Location
    }

    // Creating retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)!!
}