package com.jasminsp.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "user_favourites")
data class FavouriteData(
    @PrimaryKey(autoGenerate = true)
    val favouriteUid: Long,
    val locationName: String,
    val locationLatitude: Long,
    val locationLongitude: Long,
)

//Defining functions that can be used with the database
@Dao
interface WeatherDatabaseDao {
    //Checking database for conflict, replacing data if any
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(favouriteData: FavouriteData)

    // Get all favourites from user_favourites
    @Query("SELECT * FROM user_favourites")
    fun getAllFavourites(): LiveData<List<FavouriteData>>
}