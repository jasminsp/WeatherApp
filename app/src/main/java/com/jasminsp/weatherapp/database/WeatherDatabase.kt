package com.jasminsp.weatherapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jasminsp.weatherapp.App

//Creating the database
@Database(entities = [FavouriteData::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDatabaseDao(): WeatherDatabaseDao


    // Ensuring only one instance is created
    companion object {
        var instance: WeatherDatabase? = null
        @Synchronized
        // Ensuring that database is only created once, if null it will be created
        fun get(): WeatherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    App.appContext,
                    WeatherDatabase::class.java, "user_favourites").build()
            }
            return instance!!
        }
    }
}