package com.jasminsp.weatherapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration

class App : Application(), Configuration.Provider {
    companion object {
        lateinit var appContext: Context
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}