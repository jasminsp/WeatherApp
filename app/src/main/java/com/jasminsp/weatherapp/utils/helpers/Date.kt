package com.jasminsp.weatherapp.utils.helpers

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDate(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateFormatter.format(date)
}

fun formatTime(time: LocalDateTime): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:MM")
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