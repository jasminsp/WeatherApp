package com.jasminsp.weatherapp.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.R
import com.jasminsp.weatherapp.utils.Units
import com.jasminsp.weatherapp.utils.helpers.getCurrentTemperature
import com.jasminsp.weatherapp.utils.helpers.getMinMaxTempToday
import com.jasminsp.weatherapp.utils.helpers.getWeatherCondition
import com.jasminsp.weatherapp.weather.WeatherViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayWithLazyGrid() {
    val city = stringResource(R.string.city)
    val bigtemperature = 27
    val yourlocation = stringResource(R.string.yourlocation)
    val timestring = stringResource(R.string.time)
    val time = 5
    val moreInfo = stringResource(R.string.moreinfo)
    val todayInfo = stringResource(R.string.todaysInfo)
    val sevendayinfo = stringResource(R.string.sevendaysinfo)
    val onehour = time + 1
    val onehourtemperature = 28


    Box(modifier = Modifier.fillMaxSize()) {
        //Upper part of the screen
        Box(modifier = Modifier
            .align(Alignment.TopStart)
            .background(color = MaterialTheme.colors.primaryVariant)) {
            //Tässä oli kuva
            /*Image(modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,painter = painterResource(R.drawable.helsinki_ican), contentDescription = "")
            Image(modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,painter = painterResource(R.drawable.blue80), contentDescription = "")
            */


            // This time weather info
            Column(modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            )

            {
                Row(modifier = Modifier.fillMaxWidth()) {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(R.drawable.arrow_left_44), modifier = Modifier.requiredSize(40.dp), tint = Color.White, contentDescription ="" )
                    }

                    Text(
                        timestring + "am",
                        modifier = Modifier.absoluteOffset(100.dp),
                        style = MaterialTheme.typography.h3,
                        color = Color.White
                    )
                }
                Icon(painter = painterResource(R.drawable.nt_clear), contentDescription = "", tint = Color.White)
                Text(bigtemperature.toString() + "\u00B0" ,style = MaterialTheme.typography.h2, color = Color.White )
            }
        }
        //Lower white part
        Box(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 250.dp)){
            Image(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.aaltooo), contentDescription = "vector"
            )
            Column() {
                //Location
                Column(modifier = Modifier.padding(top = 49.dp, start = 26.dp, bottom = 20.dp)) {
                    Text(yourlocation, style = MaterialTheme.typography.h4)
                    Text(city, style = MaterialTheme.typography.subtitle2)
                }
                //Twelve next hours lazy grid
                LazyVerticalGrid(
                    cells = GridCells.Adaptive(90.dp), modifier = Modifier.padding(bottom = 100.dp, top =25.dp), content = {
                        items(12) {item ->
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 25.dp)) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(onehour.toString()+"am" )
                                    Icon(
                                        painter = painterResource(R.drawable.cloudy_icon),
                                        modifier = Modifier.requiredSize(55.dp),
                                        contentDescription = "",
                                        tint = Color.Black
                                    )
                                    Text(onehourtemperature.toString() + "\u00B0")
                                }
                            }

                        }
                    }
                )
            }


        }
        //Bottom rows
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .align(Alignment.BottomCenter)
            .background(color = Color.White)) {
            Column() {
                Column() {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp), horizontalArrangement = Arrangement.Center) {
                        Text(moreInfo, style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onPrimary )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp), horizontalArrangement = Arrangement.Center) {
                        Icon(painter = painterResource(R.drawable.down_arrow_icon),modifier = Modifier.requiredSize(15.dp), contentDescription = "", tint = MaterialTheme.colors.onPrimary )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                ) {
                    Column( modifier = Modifier.fillMaxWidth(0.5f)) {
                        Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
                        Text(todayInfo,
                            Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onSecondary)
                    }
                    Column( modifier = Modifier) {
                        Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
                        Text(sevendayinfo,
                            Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SevenDays() {
    val city = stringResource(R.string.city)
    val today = stringResource(R.string.today)
    val moreInfo = stringResource(R.string.moreinfo)
    val todayInfo = stringResource(R.string.todaysInfo)
    val sevendayinfo = stringResource(R.string.sevendaysinfo)

    val weekday = "Weekday"
    val minTemp = 15
    val maxTemp = 25

//This Box is the whole view
    Box(modifier = Modifier.fillMaxSize()) {

        //city image part
        Box(
            modifier = Modifier.align(Alignment.TopStart)
                .background(color = MaterialTheme.colors.primaryVariant)
        ) {
            /* Image(
                 modifier = Modifier.fillMaxWidth(),
                 contentScale = ContentScale.Crop,
                 painter = painterResource(R.drawable.helsinki_ican),
                 contentDescription = ""
             )
             Image(
                 modifier = Modifier.fillMaxWidth(),
                 contentScale = ContentScale.Crop,
                 painter = painterResource(R.drawable.blue80),
                 contentDescription = ""
             )*/


            // This time weather info
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            )

            {
                Row(modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left_44),
                            modifier = Modifier.requiredSize(40.dp),
                            tint = Color.White,
                            contentDescription = ""
                        )
                    }
                    Text(today,modifier = Modifier.absoluteOffset(74.dp), style = MaterialTheme.typography.h3, color = Color.White)
                }
                Text("${getCurrentTemperature(favourite)}",
                    style = MaterialTheme.typography.h2,
                    color = Color.White
                )
            }
        }
        //Lower white part
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.aaltooo), contentDescription = "vector",
            )
            Column() {
                //Location
                Column(modifier = Modifier.padding(top = 49.dp, start = 26.dp, bottom = 20.dp)) {

                    //Text(yourlocation, style = MaterialTheme.typography.h4)
                    Text(city, style = MaterialTheme.typography.subtitle2)
                }
                //Three next days
                LazyVerticalGrid(
                    cells = GridCells.Adaptive(120.dp),
                    modifier = Modifier.padding(bottom = 100.dp, top = 25.dp),
                    content = {
                        items(6) { item ->
                            Box(modifier = Modifier.fillMaxSize().padding(bottom = 25.dp)) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(weekday)
                                    Icon(
                                        painter = painterResource(R.drawable.clear_icon),
                                        modifier = Modifier.requiredSize(55.dp),
                                        contentDescription = "",
                                        tint = Color.Black
                                    )
                                    Text(minTemp.toString() + "\u00B0 | " + maxTemp.toString() + "\u00B0")
                                }
                            }

                        }
                    }
                )
            }


        }
        //Bottom rows

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .align(Alignment.BottomCenter)
                .background(color = Color.White)
        ) {
            Column() {
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            moreInfo,
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.down_arrow_icon),
                            modifier = Modifier.requiredSize(15.dp),
                            contentDescription = "",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                        Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
                        Text(
                            todayInfo,
                            Modifier.align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onSecondary
                        )
                    }
                    Column(modifier = Modifier) {
                        Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
                        Text(
                            sevendayinfo,
                            Modifier.align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(id: Int, viewModel: WeatherViewModel) {
    val wind = stringResource(R.string.wind)
    val humidity = stringResource(R.string.humidity)
    val rain = stringResource(R.string.rain)
    val uv = stringResource(R.string.uv)
    val sunrise = stringResource(R.string.sunrise)
    val sunset = stringResource(R.string.sunset)
    val high = stringResource(R.string.high)
    val low = stringResource(R.string.low)
    val favourites by viewModel.favouriteLocations.observeAsState()
    val favourite = favourites?.find { it.id == id }

    Card(
        elevation = 30.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
            .shadow(elevation = 30.dp, shape = RoundedCornerShape(size = 10.dp), clip = false)
    ){
        Column() {
            Row(modifier = Modifier.fillMaxWidth(),
            ) {
                Row(modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp)
                    .fillMaxWidth(0.6f) , verticalAlignment = Alignment.CenterVertically) {
                    Text("${getDayToday()}")
                    Icon(painter = painterResource(
                        getWeatherCondition(
                        favourite?.let { getDailyVariablesToday(it, 3) } as Int,
                        1) as Int),modifier = Modifier
                        .requiredWidth(60.dp)
                        .padding(start = 1.dp), contentDescription ="" )
                }
                Column(modifier = Modifier.padding(top = 35.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$low | $high")
                    Row() {
                        Text("${favourite?.let { getMinMaxTempToday(it, true) }}", color = Color.Blue)
                        Text(Units().divider, Modifier.padding(start = 13.dp))
                        Text("   ${favourite?.let { getMinMaxTempToday(it, false) }}", color = Color.Red)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement  =  Arrangement.SpaceBetween)
            {
                Column(modifier = Modifier.padding(top = 30.dp, start = 20.dp)) {
                    Text(wind , modifier = Modifier.padding(vertical = 3.dp))
                    Text(humidity, modifier = Modifier.padding(vertical = 3.dp) )
                    Text(rain , modifier = Modifier.padding(vertical = 3.dp))
                    Text(uv, modifier = Modifier.padding(vertical = 3.dp))
                }
                Column(modifier = Modifier.padding(top = 30.dp, start = 20.dp)) {
                    Text(" - - - - - - - - - - - - -   ", modifier = Modifier.padding(vertical = 3.dp))
                    Text("   - - - - - - - - - - -   ", modifier = Modifier.padding(vertical = 3.dp))
                    Text("- - - - - - - - - - - - - -   ", modifier = Modifier.padding(vertical = 3.dp))
                    Text("- - - - - - - - - - - - - -    ", modifier = Modifier.padding(vertical = 3.dp))
                }
                Column(modifier = Modifier.padding(top = 30.dp, end = 20.dp)) {
                    Text("${favourite?.let { getDailyVariablesToday(it, 0) }}${Units().metersSecond}", modifier = Modifier.padding(vertical = 3.dp))
                    Text("${favourite?.let { getHumidityAverage(it) }}${Units().percent}", modifier = Modifier.padding(vertical = 3.dp))
                    Text("${favourite?.let { getDailyVariablesToday(it, 4) }}${Units().milliMeter}", modifier = Modifier.padding(vertical = 3.dp))
                    Text("${favourite?.let { getDailyVariablesToday(it, 5) }}${Units().solarRadiation}", modifier = Modifier.padding(vertical = 3.dp))
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center) {
                Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(sunrise)
                    Text("${favourite?.let { getDailyVariablesToday(it, 1) }}")
                }
                Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(sunset)
                    Text("${favourite?.let { getDailyVariablesToday(it, 2) }}")
                }
            }
        }
    }
}