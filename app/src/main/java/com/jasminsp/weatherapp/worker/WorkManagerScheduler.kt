package com.jasminsp.weatherapp.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkManagerScheduler {
    fun refreshPeriodicWork(context: Context) {
        val myConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshWork = PeriodicWorkRequest.Builder(WeatherWorker::class.java, 2, TimeUnit.HOURS)
            .setInitialDelay(2, TimeUnit.SECONDS)
            .setConstraints(myConstraints)
            .addTag("weatherWorkManager")
            .build()


        WorkManager.getInstance(context).enqueueUniquePeriodicWork("weatherWorkManager",
            ExistingPeriodicWorkPolicy.REPLACE, refreshWork)
    }
}