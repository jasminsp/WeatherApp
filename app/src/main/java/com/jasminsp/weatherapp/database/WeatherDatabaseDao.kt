package com.jasminsp.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "user_favourites", primaryKeys = ["latitude", "longitude"])
data class FavouriteData(
    val locationUid: Int,
    val latitude: Double,
    val longitude: Double,
)


//Defining functions that can be used with the database
@Dao
interface WeatherDatabaseDao {
    //Checking database for conflict, replacing data if any
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(weatherData: FavouriteData)

    @Query("DELETE FROM user_favourites WHERE locationUid = :id")
    suspend fun deleteByLatLong(id: Int)

    // Get all favourites from user_favourites
    @Query("SELECT * FROM user_favourites")
    fun getAllFavourites(): LiveData<List<FavouriteData>>
}