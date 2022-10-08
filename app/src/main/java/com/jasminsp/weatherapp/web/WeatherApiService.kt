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
        var id: Int,
        var name: String,
        val latitude: Double,
        val longitude: Double,
        val utc_offset_seconds: Int,
        val timezone: String,
        val timezone_abbreviation: String,
        val current_weather: BaseCurrentWeather,
        val hourly: BaseHourly,
        val daily: BaseDaily,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MainWeather

            if (latitude != other.latitude) return false
            if (longitude != other.longitude) return false

            return true
        }

        override fun hashCode(): Int {
            var result = latitude.hashCode()
            result = 31 * result + longitude.hashCode()
            return result
        }
    }

    data class BaseCurrentWeather (
            val temperature: Double,
            val windspeed: Double,
            val weathercode: Int,
            val time: String,
        )
        data class BaseHourly (
            val time: Array<String>,
            val temperature_2m: Array<Double>,
            val weathercode: Array<Int>
        )
        data class BaseDaily (
            val time: Array<String>,
            val temperature_2m_max: Array<Double>,
            val temperature_2m_min: Array<Double>,
            val apparent_temperature_max: Array<Double>,
            val apparent_temperature_min: Array<Double>,
            val sunrise: Array<String>,
            val sunset: Array<String>,
            val weathercode: Array<Int>
        )

    // Get request to endpoint
    interface Service {
        @GET("forecast?hourly=temperature_2m,weathercode&daily=temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,weathercode&current_weather=true&timezone=auto")
        suspend fun getHourlyWeatherWithLocation(@Query("latitude") lat: Double, @Query("longitude") long: Double): MainWeather
    }

    // Creating retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)!!
}