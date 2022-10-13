package com.jasminsp.weatherapp.utils.helpers

import android.util.Log
import android.widget.TextClock
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.jasminsp.weatherapp.R
import java.lang.String.format
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*
import kotlin.time.Duration.Companion.days

fun formatDate(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateFormatter.format(date)
}

fun formatTime(time: LocalDateTime): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return timeFormatter.format(time)
}

fun getDateToday(): String {
    return formatDate(LocalDateTime.now())
}

// Get time HH:mm:ss.SSS
fun getTimeNow(): String {
    val sdf = SimpleDateFormat.getTimeInstance()
    return sdf.format(Date())
}

fun getDayToday(): DayOfWeek? {
    return LocalDateTime.now().dayOfWeek
}

fun getUTC0TimeNow(): String {
    val df = SimpleDateFormat.getTimeInstance()
    df.timeZone = TimeZone.getTimeZone("utc")
    return df.format(Date())
}

fun isDateToday(date: String): Boolean {
    if(date == getDateToday()) {
        return true
    }
    return false
}

@Composable
fun DisplayTxtClock(zone: String) {
    AndroidView(
        factory = { context ->
            TextClock(context).apply {
                // on below line we are setting 12 hour format.
                format12Hour?.let { this.format12Hour = "hh:mm a" }
                // on below line we are setting time zone.
                timeZone.let { this.timeZone = zone }
                textSize.let { this.textSize = 20f }
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    )
}

@Composable
fun WeekDay(date: String) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date2 = LocalDate.parse(date, formatter)
    val day = date2.dayOfWeek
    Text("$day", fontSize = 14.sp)
}