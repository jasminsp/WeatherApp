package com.jasminsp.weatherapp.web

import com.jasminsp.weatherapp.database.FavouriteData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WeatherApiService {
    // API base url
    private const val URL = "https://api.open-meteo.com/v1/"

    // Get request to endpoint
    interface Service {
        @GET("forecast?hourly=temperature_2m")
        suspend fun getHourlyWeatherWithLocation(@Query("latitude") lat: Double, @Query("longitude") long: Double): FavouriteData
    }

    // Creating retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)!!
}