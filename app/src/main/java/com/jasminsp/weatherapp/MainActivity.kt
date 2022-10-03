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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jasminsp.weatherapp.sensor.SensorViewModel
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme
import com.jasminsp.weatherapp.weather.WeatherViewModel

class MainActivity : ComponentActivity(), SensorEventListener {
    companion object {
        private lateinit var weatherViewModel: WeatherViewModel
        private lateinit var sensorViewModel: SensorViewModel
        private lateinit var sensorManager: SensorManager
        // do these need to be here?
        private lateinit var sensorAmbTemp: Sensor
        private lateinit var sensorRelHum: Sensor
        private lateinit var sensorAmbPres: Sensor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        setUpSensor()
        setContent {
            val navController = rememberNavController()

            weatherViewModel = WeatherViewModel(application)
            sensorViewModel = SensorViewModel()
            val tempData = sensorViewModel.tempData.observeAsState()
            val humData = sensorViewModel.humData.observeAsState()
            val presData = sensorViewModel.presData.observeAsState()

            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // The sensor data could be combined into an object
                    NavHost(navController, startDestination = "main view") {
                        composable("main view") {
                            MainView(
                                navController,
                                weatherViewModel,
                                tempData,
                                humData,
                                presData
                            )
                        } // Replace with reference to official Composable
                        composable("my location") { MyLocation(navController) } // Replace with reference to official Composable
                        composable("detail view") { DetailView(navController, tempData) } // Replace with reference to official Composable
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
        }
        if (event.sensor?.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            sensorViewModel.setHum(event.values[0])
            Log.i("SENSOR_HUM", event.values[0].toString())
        }
        if (event.sensor?.type == Sensor.TYPE_PRESSURE) {
            sensorViewModel.setPres(event.values[0])
            Log.i("SENSOR_PRES", event.values[0].toString())
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
        val sensorAmbTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        val sensorRelHum = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        val sensorAmbPres = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        /*
        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL)
        }
         */
        sensorAmbTemp.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorRelHum.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL)
        }
        sensorAmbPres.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, lat: Double, long: Double) {
    viewModel.addFavourite(lat, long)
}

// Mock composable, delete when real one is done
@Composable
fun MainView(navController: NavController, weatherViewModel: WeatherViewModel, tempData: State<Float?>, humData: State<Float?>, presData: State<Float?>) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Main view")
        Button(onClick = { navController.navigate("my location") }) {
            Text("Navigate to my location")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("detail view") }) {
            Text("Navigate to detail view")
        }

        weatherViewModel.getLocations("Berlin")
        //weatherViewModel.getFavouriteWeather(52.52437, 13.41053)
        Column {
            Text("Sensor: ${tempData.value}")
            Text("Sensor: ${humData.value}")
            Text("Sensor: ${presData.value}")
            ShowFavourites(weatherViewModel)
            SearchLocations(weatherViewModel)
        }
    }
}

// Mock composable, delete when real one is done
@Composable
fun MyLocation(navController: NavController) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("My Location")
        Button(onClick = { navController.navigateUp() }) {
            Text("Back to Main View")
        }
    }
}

// Mock composable, delete when real one is done
@Composable
fun DetailView(navController: NavController, tempData: State<Float?>) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Detail View")
        Button(onClick = { navController.navigateUp() }) {
            Text("Back to Main View")
        }
        Text(tempData.value.toString())
    }
}