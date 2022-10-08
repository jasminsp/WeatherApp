package com.jasminsp.weatherapp.utils

import androidx.compose.runtime.Composable
import com.jasminsp.weatherapp.composables.ShowFavourites
import com.jasminsp.weatherapp.weather.WeatherViewModel

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, id: Int, lat: Double, long: Double) {
    viewModel.addFavourite(id, lat, long)
}
