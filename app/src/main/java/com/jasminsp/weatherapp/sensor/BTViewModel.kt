package com.jasminsp.weatherapp.sensor

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class BTViewModel: ViewModel() {
    val scanResults = MutableLiveData<List<ScanResult>>(null)
    val fScanning = MutableLiveData(false)
    private val mResults = java.util.HashMap<String, ScanResult>()
    var tempFile: List<ScanResult>? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    val mConnectionState = MutableLiveData<Int>(-1)
    val mBPM = MutableLiveData<Int>(0)
    val bpmList = MutableLiveData<MutableList<Int>>(ArrayList())

    companion object GattAttributes {
        const val SCAN_PERIOD: Long = 5000
        const val STATE_DISCONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2

        val UUID_HEART_RATE_MEASUREMENT= UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        val UUID_HEART_RATE_SERVICE = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
        val UUID_CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

        val UUID_RUUVITAG_SOME_SERVICE = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb")

        val UUID_RUUVITAG_SOME_A = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")
        val UUID_RUUVITAG_SOME_B = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb")
        val UUID_RUUVITAG_SOME_C = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")
        val UUID_RUUVITAG_SOME_D = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")
        val UUID_RUUVITAG_SOME_E = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb")
        val UUID_RUUVITAG_SOME_F = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")
    }

    @SuppressLint("MissingPermission")
    fun scanDevices(scanner: BluetoothLeScanner) {
        Log.d("DMG", "Scanning started")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DMG", "Coroutine routing")
            fScanning.postValue(true)
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .build()
            scanner.startScan(null, settings, leScanCallback)
            delay(SCAN_PERIOD)
            scanner.stopScan(leScanCallback)
            tempFile = mResults.values.toList()
            scanResults.postValue(tempFile)
            fScanning.postValue(false)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val deviceAddress = device.address
            mResults!![deviceAddress] = result
            Log.d("DBG", "Device address: $deviceAddress (${result.isConnectable})")
        }
    }

    @SuppressLint("MissingPermission")
    fun connectDevice(context: Context, device: BluetoothDevice) {
        Log.d("DBG", "Try to connect to the address ${device.address}")
        mConnectionState.postValue(STATE_CONNECTING)
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback)
    }

    @SuppressLint("MissingPermission")
    fun disconnectDevice() {
        mBluetoothGatt?.disconnect()
    }

    private val mGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState.postValue(STATE_CONNECTED)
                Log.d("DBG", "Connected GATT service ${gatt.discoverServices()}")
                //...
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //...
                mConnectionState.postValue(STATE_DISCONNECTED)
                gatt.close()
                Log.d("DBG", "Disconnected from GATT server")
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.d("DBG", "onServicesDiscovered launched")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("DBG", "GATT success!")
                for (gattService in gatt.services) {
                    Log.d("DBG", "Service ${gattService.uuid}")
                    if (gattService.uuid == GattAttributes.UUID_RUUVITAG_SOME_A) {
                        Log.d("DBG", "Ruuvitag A service found!")
                        for (gattCharacteristic in gattService.characteristics)
                            Log.d("DBG", "Characteristic ${gattCharacteristic.uuid}")
                        /* setup the system for the notification messages */
                        val characteristic = gatt.getService(UUID_RUUVITAG_SOME_A) .getCharacteristic(
                            UUID_RUUVITAG_SOME_SERVICE)
                        Log.d("DBG", gatt.setCharacteristicNotification(characteristic, true).toString())
                        if(gatt.setCharacteristicNotification(characteristic, true)) {
                            val descriptor = characteristic.getDescriptor(
                                UUID_CLIENT_CHARACTERISTIC_CONFIG)
                            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            Log.d("DBG", "Descriptor: ${descriptor.value}")
                            val writing = gatt.writeDescriptor(descriptor)
                            Log.d("DBG", "Writer: ${writing}")

                        }
                    }
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt,
                                             characteristic: BluetoothGattCharacteristic
        ){
            val bpm = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1)
            Log.i("DBG", "BPM: $bpm")
            mBPM.postValue(bpm)
            bpmList.value?.add(bpm)
            // Call the extension function to notify
            bpmList.notifyObserver()
        }
    }

    // Kotlin Extension function to assign the LiveData to itself
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = value
    }
}