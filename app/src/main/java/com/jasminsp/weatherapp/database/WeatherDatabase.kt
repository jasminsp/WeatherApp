package com.jasminsp.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Creating the database
@Database(entities = [FavouriteData::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDatabaseDao(): WeatherDatabaseDao

    // Ensuring only one instance is created
    companion object {
        private var instance: WeatherDatabase? = null
        @Synchronized
        // Ensuring that database is only created once, if null it will be created
        fun get(context: Context): WeatherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    WeatherDatabase::class.java, "user_favourites").build()
            }
            return instance!!
        }
    }
}