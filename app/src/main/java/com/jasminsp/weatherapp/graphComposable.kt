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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun ShowGraph(dataSetA: List<Int>, dataSetB: List<Int>) {
    val entries: MutableList<Entry> = mutableListOf()
    val barList: MutableList<BarEntry> = mutableListOf()
    var isDaily = true

    // Linedata
    dataSetA.forEachIndexed { index, value ->
        entries.add(Entry((index + 0.5f), value.toFloat()))
    }

    // Bardata
    dataSetB.forEachIndexed { index, value ->
        barList.add(BarEntry((index + 0.5f), value.toFloat()))
    }

    val labels = arrayListOf(
        "Mon", "Tue", "Wed",
        "Thu", "Fri", "Sat",
        "Sun"
    )

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
                tempData.setCircleColor(R.color.black)
                tempData.mode = LineDataSet.Mode.CUBIC_BEZIER
                humData.color = R.color.teal_200
                humData.setDrawValues(false)

                val temperature = LineData(tempData)
                temperature.isHighlightEnabled = false

                val humidity = BarData(humData)
                humidity.isHighlightEnabled = false
                humidity.barWidth = 0.5f

                data.setData(temperature)
                data.setData(humidity)
                val desc = Description()
                desc.isEnabled = false

                // remove legend and gridlines
                view.axisLeft.setDrawLabels(false)
                view.axisLeft.setDrawGridLines(false)
                view.axisRight.setDrawLabels(false)
                view.axisRight.setDrawGridLines(false)
                view.xAxis.setDrawLabels(true)
                view.xAxis.setDrawGridLines(false)
                view.xAxis.position = XAxis.XAxisPosition.BOTTOM

                // adjust visible area, set up scrolling
                view.xAxis.axisMinimum = 0f
                view.xAxis.axisMaximum = barList.size.toFloat()
                view.setVisibleXRange(5f, 5f)

                // setup labeling and adjust label spacing
                view.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                view.xAxis.setCenterAxisLabels(true)
                view.xAxis.granularity = 1f
                // view.x = 7f
                // view.y = 7f

                // remove unwanted functionality
                view.setTouchEnabled(true)
                view.setScaleEnabled(false)
                view.setPinchZoom(false)

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
        ShowGraph(listOf(30,40,35,25,22,24,44,65,34,32,25,43), listOf(30, 50, 70, 65, 45, 29, 47,12,34,5,67,89))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        GraphView()
    }
}