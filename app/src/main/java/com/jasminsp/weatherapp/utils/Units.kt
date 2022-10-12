package com.jasminsp.weatherapp.utils

import androidx.compose.ui.res.stringResource
import com.jasminsp.weatherapp.R

data class Units (
    val timeAndSunriseAndSunset: String = "iso8601",
    val temperature: String = "°C",
    val temperatureShort: String = "°",
    val metersSecond: String = "m/s",
    val milliMeter: String = "mm",
    val divider: String = "|",
    val solarRadiation: String = "MJ/m²",
    val percent: String = "%"
)