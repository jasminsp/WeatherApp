package com.jasminsp.weatherapp.utils.helpers

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jasminsp.weatherapp.weather.WeatherViewModel

// Save new favourite to db
fun addFavourite(viewModel: WeatherViewModel, id: Int, lat: Double, long: Double) {
    viewModel.addFavourite(id, lat, long)
}

fun setGradient(color: Color): Brush {
    return Brush.verticalGradient(listOf(Color.Transparent, color),
        0F, 1000F)
}