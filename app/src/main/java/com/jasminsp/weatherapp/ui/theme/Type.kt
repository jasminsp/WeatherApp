package com.jasminsp.weatherapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jasminsp.weatherapp.R


val OxygenFamily = FontFamily(
    Font(R.font.oxygen, FontWeight.Normal),
    Font(R.font.oxygen_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h6 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 100.sp,
    ),
    h2 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
    ),
    h1 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    ),
    h3 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
    ),
    h5 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    ),
    h4 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    body1 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    body2 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    ),
)
