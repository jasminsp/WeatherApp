package com.jasminsp.weatherapp.web

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object LocationApiService {
        // API base url
        private const val URL = "https://geocoding-api.open-meteo.com/v1/"

        // Get request to endpoint
        interface Service {
            @GET("search?")
            suspend fun getLocationsByName(@Query("name") action: String)
        }

        // Creating retrofit
        private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(Service::class.java)!!
    }