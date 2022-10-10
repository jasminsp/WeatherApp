package com.jasminsp.weatherapp.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jasminsp.weatherapp.repository.WeatherRepository

class WeatherWorker(ctx: Context, parameters: WorkerParameters) : Worker(ctx, parameters) {
    override fun doWork(): Result {
        val repository = WeatherRepository
        return try {
            try {
                Log.i("WORKER", "Run work manager")
                repository.refreshFavourites()
                Result.success()
            } catch (error: Error) {
                Log.i("WORKER", "Exception in work manager $error")
                Result.failure()
            }
        } catch (error: Error) {
            Log.i("WORKER", "Exception in work manager $error")
            Result.failure()
        }
    }
}