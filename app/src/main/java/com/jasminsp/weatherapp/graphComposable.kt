package com.jasminsp.weatherapp

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun ShowGraph() {
    val tempList = listOf(30,40,35,25,22,24,44)
    val humList = listOf(30, 50, 70, 65, 45, 29, 47)
    val entries: MutableList<Entry> = mutableListOf()
    val barList: MutableList<BarEntry> = mutableListOf()

    tempList.forEachIndexed { index, value ->
        entries.add(Entry((index + 0.5f), value.toFloat()))
    }

    humList.forEachIndexed { index, value ->
        barList.add(BarEntry((index + 0.5f), value.toFloat()))
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
                humidity.barWidth = 0.5f
                data.setData(temperature)
                data.setData(humidity)
                val desc = Description()
                desc.isEnabled = false

                val xAxis = view.xAxis
                val yAxis = view.axisLeft
                yAxis.setDrawGridLines(false)
                yAxis.setLabelCount(5, true)
                view.axisLeft.setDrawLabels(false);
                view.axisRight.setDrawLabels(false);
                view.xAxis.setDrawLabels(false);
                view.axisLeft.setDrawGridLines(false);
                view.axisRight.setDrawGridLines(false);
                view.xAxis.setDrawGridLines(false);
                xAxis.axisMinimum = 0f
                xAxis.axisMaximum = 7f
                view.x = 7f
                view.y = 7f
                view.setTouchEnabled(false)
                view.data = data
                view.description = desc
                      view// return the view
            },
            update = { view ->
                // Update the view
                view.invalidate()
            }
        )
    }
}

@Composable
fun GraphView(){
    Column(modifier = Modifier
        .height(200.dp)
        .width(200.dp)) {
        ShowGraph()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        ShowGraph()
    }
}