package com.jasminsp.weatherapp.composables

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jasminsp.weatherapp.R
import com.jasminsp.weatherapp.utils.Units
import com.jasminsp.weatherapp.utils.helpers.*
import com.jasminsp.weatherapp.web.WeatherApiService
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HourlyWeather(favourite: WeatherApiService.MainWeather, navController: NavController) {
    var state by remember { mutableStateOf(false) }
    val hourlyVariables = getHourlyWeatherVariables(favourite, 0).take(12)
    val dailyMin = getDailyWeatherVariables(favourite, 0).take(7)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .align(Alignment.TopStart)
            .background(color = MaterialTheme.colors.primaryVariant)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(
                    getWeatherCondition(
                        favourite.current_weather.weathercode,
                        2) as Int),
                contentDescription = ""
            )
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { navController.navigate("main view") }, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(10.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left_44),
                        modifier = Modifier.requiredSize(40.dp),
                        tint = Color.White,
                        contentDescription = ""
                    )
                }
                Text(
                    favourite.name.uppercase(),
                    style = MaterialTheme.typography.subtitle2,
                    color = Color.White
                )
                Text(
                    "${favourite.current_weather.temperature.toInt()}${Units().temperatureShort}",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
        }
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 250.dp)
            ) {
                    if (state) {
                        // Hourly grid
                        LazyVerticalGrid(
                            cells = GridCells.Adaptive(120.dp),
                            modifier = Modifier.padding(bottom = 50.dp, top = 25.dp),
                            content = {
                                items(dailyMin) { min ->
                                    val index = dailyMin.indexOf(min)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 25.dp)
                                    ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 10.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Card(Modifier
                                                    .padding(4.dp),
                                                    backgroundColor = MaterialTheme.colors.onSurface,
                                                    elevation = 0.dp) {
                                                    Column(
                                                        Modifier
                                                            .fillMaxSize()
                                                            .padding(4.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Text("Monday")
                                                        Icon(
                                                            painter = painterResource(R.drawable.clear_icon),
                                                            modifier = Modifier.requiredSize(55.dp),
                                                            contentDescription = "",
                                                            tint = Color.Black
                                                        )
                                                        Row {
                                                            Text("${min.second}${Units().temperatureShort}")
                                                            Text(
                                                                Units().divider,
                                                                modifier = Modifier.padding(
                                                                    horizontal = 5.dp
                                                                )
                                                            )
                                                            Text(
                                                                "${
                                                                    getDailyWeatherByIndex(
                                                                        favourite,
                                                                        index
                                                                    )
                                                                }${Units().temperatureShort}"
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                            }
                        )
                    } else {
                        // Daily grid
                        LazyVerticalGrid(
                            cells = GridCells.Adaptive(90.dp),
                            modifier = Modifier.padding(bottom = 50.dp, top = 25.dp),
                            content = {
                                items(hourlyVariables) { hourly ->
                                    val index = hourlyVariables.indexOf(hourly)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 25.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Card(
                                                Modifier
                                                    .padding(4.dp),
                                                    backgroundColor = MaterialTheme.colors.onSurface,
                                                elevation = 0.dp) {
                                                Column(
                                                    Modifier
                                                        .fillMaxSize()
                                                        .padding(4.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(formatTime(LocalDateTime.parse(hourly.first)))
                                                    Icon(
                                                        painter = painterResource(
                                                            getWeatherCondition(
                                                                getHourlyWeatherCodeByIndex(
                                                                    favourite,
                                                                    index
                                                                ), 1
                                                            ) as Int
                                                        ),
                                                        modifier = Modifier.requiredSize(55.dp),
                                                        contentDescription = "",
                                                        tint = Color.Black
                                                    )
                                                    Text("${hourly.second.toInt()}${Units().temperatureShort}")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colors.onSurface)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable { state = !state }) {
                            Divider(
                                color = if (!state) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                                thickness = 2.dp
                            )
                            Text(
                                stringResource(id = R.string.todaysInfo),
                                Modifier.align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.h5,
                                color = if (!state) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary
                            )
                        }
                        Column(modifier = Modifier.clickable { state = !state }) {
                            Divider(
                                color = if (!state) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
                                thickness = 2.dp
                            )
                            Text(
                                stringResource(id = R.string.sevendaysinfo),
                                Modifier.align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.h5,
                                color = if (!state) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary
                            )
                        }
                    }
                }
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 740.dp)) {
                Column() {
                    DetailCard(favourite)
                    GraphCard(favourite)
                }
            }
    }
}

        @Composable
        fun DetailCard(favourite: WeatherApiService.MainWeather) {
            val wind = stringResource(R.string.wind)
            val humidity = stringResource(R.string.humidity)
            val rain = stringResource(R.string.rain)
            val uv = stringResource(R.string.uv)
            val sunrise = stringResource(R.string.sunrise)
            val sunset = stringResource(R.string.sunset)
            val high = stringResource(R.string.high)
            val low = stringResource(R.string.low)

                Card(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(size = 10.dp),
                            clip = false
                        ),

                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, start = 20.dp)
                                    .fillMaxWidth(0.6f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${getDayToday()}")
                                Icon(
                                    painter = painterResource(
                                        getWeatherCondition(
                                            getDailyVariablesToday(
                                                favourite,
                                                3
                                            ) as Int, 1
                                        ) as Int
                                    ),
                                    modifier = Modifier
                                        .requiredWidth(60.dp)
                                        .padding(start = 1.dp), contentDescription = ""
                                )
                            }
                            Column(
                                modifier = Modifier.padding(top = 35.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("$low${Units().temperatureShort} | $high${Units().temperatureShort}")
                                Row {
                                    Text(
                                        "${getMinMaxTempToday(favourite, true)}",
                                        color = Color.Blue
                                    )
                                    Text(Units().divider, Modifier.padding(start = 13.dp))
                                    Text(
                                        "   ${getMinMaxTempToday(favourite, false)}",
                                        color = Color.Red
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Column(modifier = Modifier.padding(top = 30.dp, start = 20.dp)) {
                                Text(wind, modifier = Modifier.padding(vertical = 3.dp))
                                Text(humidity, modifier = Modifier.padding(vertical = 3.dp))
                                Text(rain, modifier = Modifier.padding(vertical = 3.dp))
                                Text(uv, modifier = Modifier.padding(vertical = 3.dp))
                            }
                            Column(modifier = Modifier.padding(top = 30.dp, start = 20.dp)) {
                                Text(
                                    " - - - - - - - - - - - - -   ",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                                Text(
                                    "   - - - - - - - - - - -   ",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                                Text(
                                    "- - - - - - - - - - - - - -   ",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                                Text(
                                    "- - - - - - - - - - - - - -    ",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                            }
                            Column(modifier = Modifier.padding(top = 30.dp, end = 20.dp)) {
                                Text(
                                    "${
                                        getDailyVariablesToday(
                                            favourite,
                                            0
                                        )
                                    }${Units().metersSecond}",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                                Text(
                                    "${getHumidityAverage(favourite)}${Units().percent}",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                                Text(
                                    "${getDailyVariablesToday(favourite, 4)}${Units().milliMeter}",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                                Text(
                                    "${
                                        getDailyVariablesToday(
                                            favourite,
                                            5
                                        )
                                    }${Units().solarRadiation}",
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(sunrise)
                                Text("${getDailyVariablesToday(favourite, 1)}")
                            }
                            Column(
                                Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(sunset)
                                Text("${getDailyVariablesToday(favourite, 2)}")
                            }
                        }
                    }
                }
            }

@Composable
fun GraphCard(favourite: WeatherApiService.MainWeather) {
    val temperatureVariables = getHourlyWeatherVariables(favourite, 0).take(12)
    val humidityVariables = getHourlyWeatherVariables(favourite, 1).take(12)

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(size = 10.dp),
                clip = false
            ),
    ) {
        Column {
            GraphView(temperatureVariables, humidityVariables)
        }
    }
}

@Composable
fun ShowGraph(dataSetA: List<Pair<String, Double>>, dataSetB: List<Pair<String, Double>>) {
    val entries: MutableList<Entry> = mutableListOf()
    val barList: MutableList<BarEntry> = mutableListOf()
    val labelList: ArrayList<String> = arrayListOf()

    // Linedata
    dataSetA.forEachIndexed { index, value ->
        entries.add(Entry((index + 0.5f), value.second.toFloat()))
        labelList.add(value.first[11].toString() + value.first[12].toString())
    }

    // Bardata
    dataSetB.forEachIndexed { index, value ->
        barList.add(BarEntry((index + 0.5f), value.second.toFloat()))
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
                tempData.color = android.graphics.Color.BLUE
                tempData.setCircleColor(android.graphics.Color.WHITE)
                tempData.mode = LineDataSet.Mode.CUBIC_BEZIER
                tempData.lineWidth = 3f
                tempData.valueTextSize = 14f
                humData.color = android.graphics.Color.GRAY
                humData.setDrawValues(true)
                humData.valueTextSize = 14f

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
                view.xAxis.valueFormatter = IndexAxisValueFormatter(labelList)
                view.xAxis.setCenterAxisLabels(true)
                view.xAxis.granularity = 1f

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
fun GraphView(tempData: List<Pair<String, Double>>, humData: List<Pair<String, Double>>){
    Column(modifier = Modifier
        .height(400.dp)
        .width(400.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        Row() {
            Text(stringResource(R.string.graphdeschum), color = Color.Gray)
            Spacer(Modifier.width(16.dp))
            Text(stringResource(R.string.graphdesctemp), color = Color.Blue)
        }
        ShowGraph(tempData, humData)
    }
}