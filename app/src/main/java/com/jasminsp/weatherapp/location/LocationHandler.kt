package com.jasminsp.weatherapp.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationHandler(private var context: Context, var viewModel: LocationViewModel) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: LiveData<Location> = _currentLocation

    fun getMyLocation() {
        Log.d("DMG", "getMyLocation called")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("DMG", "We gots shit A, $location")
                if (location!=null) {
                    viewModel.updateLocation(location)
                    Log.d("DMG", "We gots shit B")
                } else {
                    Log.d("DBM", "Location value is $location")
                }
            }
        } else {
            Log.d("DMG", "Problem with fetching location")
        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                currentLocation.value?.let { viewModel.updateLocation(it) }
            }
        }
    }
}