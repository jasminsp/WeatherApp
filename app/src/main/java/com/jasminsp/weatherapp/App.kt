package com.jasminsp.weatherapp

import android.app.Application
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class App : Application() {
    private val appScope = CoroutineScope(Dispatchers.Main)
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("QQQ", "MyApp onCreate")
        appContext = applicationContext
    }
}