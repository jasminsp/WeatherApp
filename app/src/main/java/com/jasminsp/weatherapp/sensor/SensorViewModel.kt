package com.jasminsp.weatherapp.sensor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel() {
    var tempData = MutableLiveData(0f)
    var humData = MutableLiveData(0f)
    var presData = MutableLiveData(0f)
    var dewPoint = calculateDewPoint(tempData.value!!, humData.value!!)

    // functions needed if tempData is set to private
    fun setTemp(temp: Float) {
        tempData.value = temp
    }

    fun getTemp(): Float? {
        return tempData.value
    }

    fun setHum(hum: Float) {
        humData.value = hum
    }

    fun getHum(): Float? {
        return humData.value
    }

    fun setPres(hum: Float) {
        presData.value = hum
    }

    fun getPres(): Float? {
        return presData.value
    }

    // based on https://github.com/PacktPublishing/Android-Sensor-Programming-By-Example
    fun calculateDewPoint(temperature: Float, relativeHumidity: Float): Float {
        var dewP = 0f
        val m = 17.62f
        val Tn = 243.12f
        Log.i("DEW", "Calculating dew point")
        dewP =
            (Tn * ((Math.log((relativeHumidity / 100).toDouble()) + m * temperature / (Tn + temperature)) / (m - (Math.log(
                (relativeHumidity / 100).toDouble()
            ) + m * temperature / (Tn + temperature))))).toFloat()
        Log.i("DEW", dewP.toString())
        // returns NaN if temperature and/or humidity is 0.0
        return dewP
    }
}