package com.jasminsp.weatherapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jasminsp.weatherapp.sensor.BTViewModel
import com.jasminsp.weatherapp.sensor.SensorViewModel
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme
import com.jasminsp.weatherapp.weather.WeatherViewModel
import com.ruuvi.station.bluetooth.FoundRuuviTag
import com.ruuvi.station.bluetooth.IRuuviTagScanner
import com.ruuvi.station.bluetooth.RuuviRangeNotifier

class MainActivity : ComponentActivity(), SensorEventListener, IRuuviTagScanner.OnTagFoundListener {
    companion object {
        private lateinit var weatherViewModel: WeatherViewModel
        private lateinit var sensorViewModel: SensorViewModel
        private lateinit var btViewModel: BTViewModel
        private lateinit var sensorManager: SensorManager
        private lateinit var ruuviRangeNotifier: IRuuviTagScanner
        private var mBluetoothAdapter: BluetoothAdapter? = null
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isBTScanPermissionGranted = false
    private var isBTConnectPermissionGranted = false
    private var isLocationPermissionGranted = false
    private var isBTAdminPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        ruuviRangeNotifier = RuuviRangeNotifier(application, "MainActivity")
        mBluetoothAdapter = mBluetoothManager.adapter
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: isLocationPermissionGranted
            isBTScanPermissionGranted = permissions[Manifest.permission.BLUETOOTH_SCAN] ?: isBTScanPermissionGranted
            isBTConnectPermissionGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] ?: isBTConnectPermissionGranted
            isBTAdminPermissionGranted = permissions[Manifest.permission.BLUETOOTH_ADMIN] ?: isBTAdminPermissionGranted

            Log.i("PERM", "")
        }

        requestPermission()
        setUpSensor()
        startScanning()
        setContent {
            val navController = rememberNavController()

            weatherViewModel = WeatherViewModel(application)
            sensorViewModel = SensorViewModel()
            btViewModel = BTViewModel()
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val tempData = sensorViewModel.tempData.observeAsState()

            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController, startDestination = "main view") {
                        composable("main view") { MainView(navController, weatherViewModel, btViewModel, mBluetoothAdapter!!, tempData) } // Replace with reference to official Composable
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
        ruuviRangeNotifier.stopScanning()
    }

    fun startScanning() {
        ruuviRangeNotifier.startScanning(this)
    }

    override fun onTagFound(tag: FoundRuuviTag) {
        // Found tags will appear here
        Log.d(tag.id, tag.temperature.toString())
        Log.d(tag.id, tag.accelX.toString())
    }

    private fun setUpSensor() {
        Log.i("SENSOR", "setUpSensor called")
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.also {
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

        isBTScanPermissionGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED

        isBTConnectPermissionGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()

        if (!isLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!isBTScanPermissionGranted){
            permissionRequest.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        if (!isBTConnectPermissionGranted){
            permissionRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (!isBTAdminPermissionGranted){
            permissionRequest.add(Manifest.permission.BLUETOOTH_ADMIN)
        }

        if (permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }
}

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, lat: Double, long: Double) {
    viewModel.addFavourite(lat, long)
}

// Mock composable, delete when real one is done
@Composable
fun MainView (navController: NavController, weatherViewModel: WeatherViewModel, btViewModel: BTViewModel, mBluetoothAdapter: BluetoothAdapter, tempData: State<Float?>) {
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
            ShowBTDevices(mBluetoothAdapter, btViewModel, LocalContext.current)
            ShowFavourites(weatherViewModel)
            SearchLocations(weatherViewModel)
        }
    }
}

// Mock composable, delete when real one is done
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

// Mock composable, delete when real one is done
@Composable
fun MyLocation (navController: NavController) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("My Location")
        Button(onClick = { navController.navigateUp() }) {
            Text("Back to Main View")
        }
    }
}