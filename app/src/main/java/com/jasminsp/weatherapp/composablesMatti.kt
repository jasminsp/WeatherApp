package com.jasminsp.weatherapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.sensor.BTViewModel

@Composable
fun ShowBTDevices(mBluetoothAdapter: BluetoothAdapter, btViewModel: BTViewModel, context: Context) {
    val value: List<ScanResult>? by btViewModel.scanResults.observeAsState(null)
    val fScanning: Boolean by btViewModel.fScanning.observeAsState(false)
    val scrollState = rememberScrollState()
    val bpm: Int by btViewModel.mBPM.observeAsState(0)
    val connectionState: Int by btViewModel.mConnectionState.observeAsState(-1)
    var editable by remember { mutableStateOf(true)}

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    btViewModel.scanDevices(mBluetoothAdapter.bluetoothLeScanner)
                }, enabled = !fScanning) {
                if (fScanning) {
                    Text(text = "Scanning in progress")
                    editable = false
                } else {
                    Text(text = "Start scanning")
                    editable = true
                }
            }
            Text(
                text = if (bpm != 0) "$bpm BPM" else "Testing",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary
            )
            Spacer(Modifier. width(35.dp))
            Text(text = when(connectionState) {
                BTViewModel.STATE_CONNECTED-> "Connected"
                BTViewModel.STATE_CONNECTING -> "Connecting..."
                BTViewModel.STATE_DISCONNECTED -> "Disconnected"
                else -> ""
            })
            Spacer(Modifier. height(16.dp))
            if (bpm != 0 ) Button(
                onClick = {
                    Toast.makeText(context, "You clicked it!", Toast.LENGTH_SHORT)
                    // viewModel.disconnectDevice()
                },
                modifier = Modifier
                    .padding(9.dp)
            ) {
                Text("Tap me for a nice graph!")
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .padding(8.dp)) {
            value?.distinct()?.forEach { scanResult ->
                ResultRow(result = scanResult, btViewModel)
            }
        }
    }
}

@Composable
fun ResultRow(result: ScanResult, viewModel: BTViewModel) {
    val context = LocalContext.current
    val scanRecord = result.scanRecord
    Row(modifier = Modifier
        .padding(4.dp)
        .clickable(
            onClick = {
                Log.d("DBG", "Trying to connect to $result (${result.isConnectable})")
                // viewModel.connectDevice(context, result.device)
            }
        ),
    ) {
        Text(
            text = "${result.device} ${scanRecord?.deviceName ?: "N/A"} ${result.rssi}",
            color = if (result.isConnectable) MaterialTheme.colors.primary else androidx.compose.ui.graphics.Color.Gray)
    }
}