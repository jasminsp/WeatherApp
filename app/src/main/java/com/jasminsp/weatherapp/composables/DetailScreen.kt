package com.jasminsp.weatherapp.composables

import android.util.Log
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jasminsp.weatherapp.R
import com.jasminsp.weatherapp.utils.Units
import com.jasminsp.weatherapp.utils.helpers.*
import com.jasminsp.weatherapp.web.WeatherApiService
import java.lang.String.format
import java.text.DateFormat
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
                                                        WeekDay(min.first)
                                                        Icon(
                                                            painter = painterResource(R.drawable.clear_icon),
                                                            modifier = Modifier.requiredSize(55.dp),
                                                            contentDescription = "",
                                                            tint = Color.Black
                                                        )
                                                        Row {
                                                            Text("${String.format("%.0f", min.second)}${Units().temperatureShort}")
                                                            Text(
                                                                Units().divider,
                                                                modifier = Modifier.padding(
                                                                    horizontal = 5.dp
                                                                )
                                                            )
                                                            Text(
                                                                "${String.format("%.0f", getDailyWeatherByIndex(
                                                                    favourite,
                                                                    index
                                                                ))
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
                DetailCard(favourite)
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
                    elevation = 30.dp,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 20.dp)
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(size = 10.dp),
                            clip = false
                        )
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
                                Text("$low | $high")
                                Row {
                                    Text(
                                        "${getMinMaxTempToday(favourite, true)}${Units().temperatureShort}",
                                        color = Color.Blue
                                    )
                                    Text(Units().divider, Modifier.padding(start = 13.dp))
                                    Text(
                                        "   ${getMinMaxTempToday(favourite, false)}${Units().temperatureShort}",
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