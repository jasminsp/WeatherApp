package com.jasminsp.weatherapp

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun ShowGraph() {
    val bpmList = listOf(30,40,35,25,22)
    val dayList = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val entries: MutableList<Entry> = mutableListOf()
    val barList: MutableList<BarEntry> = mutableListOf(BarEntry(0.5f, 3f), BarEntry( 1.5f,5f), BarEntry(2.5f, 1f), BarEntry(3.5f, 2f), BarEntry(4.5f, 9f))

    bpmList.forEachIndexed { index, bpm ->
        entries.add(Entry((0.5 + index).toFloat(), bpm.toFloat()))
    }

    Column(
        modifier = Modifier
            .padding(9.dp),
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context: Context ->
                val view = CombinedChart(context)
                val data = CombinedData()
                view.legend.isEnabled = false
                val tempData = LineDataSet(entries, "Temp")
                val humData = BarDataSet(barList, "Hum")
                tempData.color = R.color.purple_700
                humData.color = R.color.black
                val temperature = LineData(tempData)
                val humidity = BarData(humData)
                data.setData(temperature)
                data.setData(humidity)
                val desc = Description()
                desc.text = "Beats Per Minute"
                view.description = desc
                view.data = data

                      view// return the view
            },
            update = { view ->
                // Update the view
                view.invalidate()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        ShowGraph()
    }
}