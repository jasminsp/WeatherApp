package com.jasminsp.weatherapp.utils

import android.widget.Toast
import com.jasminsp.weatherapp.App

    // For showing toast after error occured
    fun errorToast(error: Error) {
        Toast.makeText(
            App.appContext, "$error",
            Toast.LENGTH_LONG).show()
    }
