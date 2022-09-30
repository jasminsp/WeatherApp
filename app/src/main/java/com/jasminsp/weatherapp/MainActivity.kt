package com.jasminsp.weatherapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.jasminsp.weatherapp.sensor.SensorViewModel
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme
import com.jasminsp.weatherapp.weather.WeatherViewModel
import kotlinx.coroutines.*
import java.io.Console

class MainActivity : ComponentActivity(), SensorEventListener {
    companion object {
        private lateinit var weatherViewModel: WeatherViewModel
        private lateinit var sensorViewModel: SensorViewModel
        private lateinit var sensorManager: SensorManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSensor()
        setContent {
            weatherViewModel = WeatherViewModel(application)
            sensorViewModel = SensorViewModel()
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val tempData = sensorViewModel.tempData.observeAsState()

            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    weatherViewModel.getLocations("Berlin")
                    Text(tempData.value.toString())
                    //weatherViewModel.getFavouriteWeather(52.52437, 13.41053)
                    Column {
                        ShowFavourites(weatherViewModel)
                        SearchLocations(weatherViewModel)
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        return
    }

    override fun onSensorChanged(event: SensorEvent) {
        // TODO: update an observed variable when the received sensor value changes
        if (event.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            sensorViewModel.setTemp(event.values[0])
            Log.i("SENSOR", event.values[0].toString())
        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        setUpSensor()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        // unregister the listener when activity pauses to save battery, change if want to work in background
        sensorManager.unregisterListener(this)
    }

    private fun setUpSensor() {
        Log.i("SENSOR", "setUpSensor called")
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, lat: Double, long: Double) {
        viewModel.addFavourite(lat, long)
}