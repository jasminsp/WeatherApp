package com.jasminsp.weatherapp.sensor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel() {
    // variables to store data from internal sensors
    var tempData = MutableLiveData(0f)
    var humData = MutableLiveData(0f)
    var presData = MutableLiveData(0f)

    //variables to store data from a RuuviTag
    var tempDataTag = MutableLiveData(0f)
    var humDataTag = MutableLiveData(0f)
    var presDataTag = MutableLiveData(0f)

    fun dewPoint(option: Boolean) {

    }

    // TODO: Would be nice to get sensor data with Math.floored value (1 decimal?) and not do the conversion in MainActivity

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

    // returns NaN if temperature and/or humidity is 0.0
    fun calculateDewPoint(option: Boolean): Float {
        var relativeHumidity = if (option) humDataTag.value else humData.value
        var temperature = if (option) tempDataTag.value else tempData.value
        var dewP = 0f
        val m = 17.62f
        val Tn = 243.12f
        Log.i("DEW", "Calculating dew point")
        if (relativeHumidity != null && temperature != null) {
            dewP =
                (Tn * ((Math.log((relativeHumidity / 100).toDouble()) + m * temperature / (Tn + temperature)) / (m - (Math.log(
                    (relativeHumidity / 100).toDouble()
                ) + m * temperature / (Tn + temperature))))).toFloat()
            Log.i("DEW", dewP.toString())
            } else {
                dewP = 0f
        }
        Log.i("DEW", dewP.toString())
        return dewP
    }
}