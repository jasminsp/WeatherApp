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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSensor()
        setContent {
            val navController = rememberNavController()

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
                    NavHost(navController, startDestination = "main view") {
                        composable("main view") { MainView(navController) }
                        composable("my location") { MyLocation(navController) }
                        composable("detail view") { DetailView(navController, tempData) }
                    }
                    weatherViewModel.getLocations("Berlin")
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

@Composable
fun MainView (navController: NavController) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Main view")
        Button(onClick = { navController.navigate("my location") }) {
            Text("Navigate to my location")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("detail view") }) {
            Text("Navigate to detail view")
        }
    }
}

@Composable
fun DetailView (navController: NavController, tempData: State<Float?>) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Detail View")
        Button(onClick = { navController.navigateUp() }) {
            Text("Back to Main View")
        }
        Text(tempData.value.toString())
    }
}

@Composable
fun MyLocation (navController: NavController) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("My Location")
        Button(onClick = { navController.navigateUp() }) {
            Text("Back to Main View")
        }
    }
}