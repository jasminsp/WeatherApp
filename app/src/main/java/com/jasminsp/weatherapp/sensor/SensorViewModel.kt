package com.jasminsp.weatherapp.sensor

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

    fun setTemp(temp: Float) {
        tempData.value = temp
    }

    fun setHum(hum: Float) {
        humData.value = hum
    }

    fun setPres(hum: Float) {
        presData.value = hum
    }

    // returns NaN if temperature and/or humidity is 0.0
    fun calculateDewPoint(option: Boolean): Float {
        val relativeHumidity = if (option) humDataTag.value else humData.value
        val temperature = if (option) tempDataTag.value else tempData.value
        var dewP = 0f
        val m = 17.62f
        val tN = 243.12f
        if (relativeHumidity != null && temperature != null) {
            dewP =
                (tN * ((Math.log((relativeHumidity / 100).toDouble()) + m * temperature / (tN + temperature)) / (m - (Math.log(
                    (relativeHumidity / 100).toDouble()
                ) + m * temperature / (tN + temperature))))).toFloat()
            } else {
                dewP = 0f
        }
        return dewP
    }
}