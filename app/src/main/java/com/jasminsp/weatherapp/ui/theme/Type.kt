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

val NotoFamily = FontFamily(
    Font(R.font.noto_serif, FontWeight.Normal),
    Font(R.font.noto_serif_bold, FontWeight.Bold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    h2 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
    ),
    h1 = TextStyle(
        fontFamily = OxygenFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
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
        fontFamily = NotoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    body2 = TextStyle(
        fontFamily = NotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp),


    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
    )
