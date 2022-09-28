package com.jasminsp.weatherapp.web

import androidx.room.TypeConverter
import com.jasminsp.weatherapp.database.FavouriteData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WeatherApiService {
    // API base url
    private const val URL = "https://api.open-meteo.com/v1/"

    // Storing weather info into variables before sending to database
    data class MainWeather (
        val latitude: Double,
        val longitude: Double,
        val timezone: String,
        val timezone_abbreviation: String,
        val current_weather: BaseCurrentWeather,
        val hourly: BaseHourly,
        val daily: BaseDaily,
    )
        data class BaseCurrentWeather (
            val temperature: Double,
            val windspeed: Double,
            val weathercode: Byte,
            val time: String,
        )
        data class BaseHourly (
            val time: Array<String>,
            val temperature_2m: Array<Double>,
        )
        data class BaseDaily (
            val time: Array<String>,
            val temperature_2m_max: Array<Double>,
            val temperature_2m_min: Array<Double>,
            val apparent_temperature_max: Array<Double>,
            val apparent_temperature_min: Array<Double>,
            val sunrise: Array<String>,
            val sunset: Array<String>,
        )

    // Get request to endpoint
    interface Service {
        @GET("forecast?hourly=temperature_2m&daily=temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset&current_weather=true&timezone=auto")
        suspend fun getHourlyWeatherWithLocation(@Query("latitude") lat: Double, @Query("longitude") long: Double): MainWeather
    }

    // Creating retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)!!
}