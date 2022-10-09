package com.jasminsp.weatherapp.utils

import android.app.Application
import android.widget.Toast

// For showing toast after error occured
    fun errorToast(error: Error) {
        Toast.makeText(
            Application().applicationContext, "$error",
            Toast.LENGTH_LONG).show()
    }
