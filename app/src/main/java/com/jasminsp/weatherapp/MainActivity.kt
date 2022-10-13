package com.jasminsp.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jasminsp.weatherapp.location.LocationHandler
import com.jasminsp.weatherapp.location.LocationViewModel
import com.jasminsp.weatherapp.utils.Units
import com.jasminsp.weatherapp.composables.*
import com.jasminsp.weatherapp.sensor.SensorViewModel
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme
import com.jasminsp.weatherapp.utils.createNotificationChannel
import com.jasminsp.weatherapp.utils.showNotification
import com.jasminsp.weatherapp.weather.WeatherViewModel
import com.jasminsp.weatherapp.web.WeatherApiService
import com.jasminsp.weatherapp.worker.WorkManagerScheduler
import com.ruuvi.station.bluetooth.FoundRuuviTag
import com.ruuvi.station.bluetooth.IRuuviTagScanner
import com.ruuvi.station.bluetooth.RuuviRangeNotifier

class MainActivity : ComponentActivity(), SensorEventListener, IRuuviTagScanner.OnTagFoundListener {
    companion object {
        private lateinit var weatherViewModel: WeatherViewModel
        private lateinit var sensorViewModel: SensorViewModel
        private lateinit var locationViewModel: LocationViewModel
        private lateinit var sensorManager: SensorManager
        private lateinit var ruuviRangeNotifier: IRuuviTagScanner
        private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
        // helper variables for permission check
        private var isBTScanPermissionGranted = false
        private var isBTConnectPermissionGranted = false
        private var isLocationPermissionGranted = false
        private var isBTAdminPermissionGranted = false
    }

    // placed outside companion object to avoid memory leak
    private lateinit var locationHandler: LocationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appContext = applicationContext
        WorkManagerScheduler.refreshPeriodicWork(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        ruuviRangeNotifier = RuuviRangeNotifier(application, "MainActivity")
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: isLocationPermissionGranted
            isBTScanPermissionGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] ?: isBTScanPermissionGranted
            isBTConnectPermissionGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] ?: isBTConnectPermissionGranted
            isBTAdminPermissionGranted = permissions[Manifest.permission.BLUETOOTH_ADMIN] ?: isBTAdminPermissionGranted
        }

        locationViewModel = LocationViewModel()
        sensorViewModel = SensorViewModel()
        locationHandler = LocationHandler(applicationContext, locationViewModel)

        requestPermission()
        setUpSensor()
        startScanning()
        locationHandler.getMyLocation()

        setContent {
            val navController = rememberNavController()
            weatherViewModel = WeatherViewModel()

            LaunchedEffect(Unit) {
                createNotificationChannel(Units().channelId, applicationContext)
            }

            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // The sensor data could be combined into an object
                    NavHost(navController, startDestination = "main view") {
                        composable("main view") { MainView(navController, weatherViewModel, sensorViewModel, locationViewModel) } // Replace with reference to official Composable
                        composable("my location") { GraphView() } // Replace with reference to official Composable
                        composable("detail view/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                            val id = it.arguments?.getInt("id") ?: 0
                            DetailView(navController, weatherViewModel, id) }
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
        }
        if (event.sensor?.type == Sensor.TYPE_PRESSURE) {
            sensorViewModel.setPres(event.values[0])
        }
    }

    override fun onResume() {
        // Register a listener for the internal sensors
        setUpSensor()
        // Start scanning for RuuviTags
        startScanning()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        // unregister the listener when activity pauses to save battery, change if want to work in background
        sensorManager.unregisterListener(this)
        // stop scanning for RuuviTags when the activity is in background
        ruuviRangeNotifier.stopScanning()
    }

    private fun startScanning() {
        ruuviRangeNotifier.startScanning(this)
    }

    override fun onTagFound(tag: FoundRuuviTag) {
        // Log info on found RuuviTags
        //Log.d("RUUVI", tag.temperature.toString())
        //Log.d("RUUVI", tag.humidity.toString())
        //Log.d("RUUVI", tag.pressure.toString())

        // Update ruuvitag info in SensorViewModel
        if (tag.temperature != null) {
            sensorViewModel.tempDataTag.value = tag.temperature?.toFloat()
        }
        if (tag.humidity != null) {
            sensorViewModel.humDataTag.value = tag.humidity?.toFloat()
        }
        if (tag.pressure != null) {
            sensorViewModel.presDataTag.value = tag.pressure?.toFloat()
            }
    }

    private fun setUpSensor() {
        Log.i("SENSOR", "setUpSensor called")
        val sensorAmbTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        val sensorRelHum = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        val sensorAmbPres = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

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

    private fun requestPermission() {
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        isBTAdminPermissionGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.BLUETOOTH_ADMIN
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            isBTScanPermissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            isBTConnectPermissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        }

        val permissionRequest: MutableList<String> = ArrayList()

        if (!isLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!isBTScanPermissionGranted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
        }

        if (!isBTConnectPermissionGranted){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        if (!isBTAdminPermissionGranted){
            permissionRequest.add(Manifest.permission.BLUETOOTH_ADMIN)
        }

        if (permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }
}

@Composable
fun MainView(navController: NavController, weatherViewModel: WeatherViewModel, sensorViewModel: SensorViewModel, locationViewModel: LocationViewModel) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(weatherViewModel)
        ShowSearchResult(navController, weatherViewModel)
        YourLocationCard(sensorViewModel, weatherViewModel, locationViewModel)
        ShowFavourites(navController, weatherViewModel)
    }
}

// Mock composable, delete when real one is done
@Composable
fun DetailView(navController: NavController, viewModel: WeatherViewModel, id: Int) {
    val favourites by viewModel.favouriteLocations.observeAsState()
    val favourite = favourites?.find { it.id == id }

    Box(Modifier.verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1500.dp)

            ) {
                if (favourite != null) {
                        HourlyWeather(favourite, navController)
                }
            }
    }
}