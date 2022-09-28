package com.jasminsp.weatherapp.sensor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorViewModel : ViewModel() {
    var tempData = MutableLiveData(0f)

    // functions needed if tempData is set to private
    fun setTemp(temp: Float) {
        tempData.value = temp
    }

    fun getTemp(): Float? {
        return tempData.value
    }
}