package com.jasminsp.weatherapp.sensor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel() {
    var tempData = MutableLiveData(0f)
    var humData = MutableLiveData(0f)
    var presData = MutableLiveData(0f)

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
}