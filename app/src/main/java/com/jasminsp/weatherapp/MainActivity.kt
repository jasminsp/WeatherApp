package com.jasminsp.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme
import com.jasminsp.weatherapp.weather.WeatherViewModel
import kotlinx.coroutines.*
import java.io.Console

class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var weatherViewModel: WeatherViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            weatherViewModel = WeatherViewModel(application)
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        ShowFavourites(weatherViewModel)
                        SearchLocations(weatherViewModel)
                    }
                }
            }
        }
    }
}

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, lat: Double, long: Double) {
        viewModel.addFavourite(lat, long)
}