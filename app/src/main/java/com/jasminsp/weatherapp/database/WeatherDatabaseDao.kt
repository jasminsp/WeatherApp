package com.jasminsp.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.internal.bind.util.ISO8601Utils
import com.jasminsp.weatherapp.web.WeatherApiService
import java.text.DateFormat

@Entity(tableName = "user_favourites", indices = [Index(value = ["latitude", "longitude"], unique = true)])
data class FavouriteData(
    @PrimaryKey(autoGenerate = true)
    val locationUid: Long,
    val latitude: Double,
    val longitude: Double,
)


//Defining functions that can be used with the database
@Dao
interface WeatherDatabaseDao {
    //Checking database for conflict, replacing data if any
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(weatherData: FavouriteData)

    @Query("DELETE FROM user_favourites WHERE latitude = :lat AND longitude = :long")
    fun deleteByLatLong(lat: Double, long: Double)

    // Get all favourites from user_favourites
    @Query("SELECT * FROM user_favourites")
    fun getAllFavourites(): LiveData<List<FavouriteData>>
}