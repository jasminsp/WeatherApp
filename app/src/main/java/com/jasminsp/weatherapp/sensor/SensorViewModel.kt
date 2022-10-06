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

    // functions needed if tempData is set to private
    fun setTemp(temp: Float) {
        tempData.value = temp
    }

    fun getTemp(): Float? {
        return tempData.value
    }
}