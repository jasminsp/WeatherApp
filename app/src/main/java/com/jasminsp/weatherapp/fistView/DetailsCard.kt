package com.jasminsp.weatherapp.fistView


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DetailCard(){
    val weekday = stringResource(com.jasminsp.weatherapp.R.string.weekday)
    val wind = stringResource(com.jasminsp.weatherapp.R.string.wind)
    val humidity = stringResource(com.jasminsp.weatherapp.R.string.humidity)
    val rain = stringResource(com.jasminsp.weatherapp.R.string.rain)
    val uv = stringResource(com.jasminsp.weatherapp.R.string.uv)
    val sunrise = stringResource(com.jasminsp.weatherapp.R.string.sunrise)
    val sunset = stringResource(com.jasminsp.weatherapp.R.string.sunset)
    val sunrisetime = stringResource(com.jasminsp.weatherapp.R.string.sunrisetime)
    val sunsettime = stringResource(com.jasminsp.weatherapp.R.string.sunsettime)
    val high = stringResource(com.jasminsp.weatherapp.R.string.high)
    val low = stringResource(com.jasminsp.weatherapp.R.string.low)
    val min = stringResource(com.jasminsp.weatherapp.R.string.min)
    val max = stringResource(com.jasminsp.weatherapp.R.string.max)
    val metersSecond = stringResource(com.jasminsp.weatherapp.R.string.meters_second)
    val percent = stringResource(com.jasminsp.weatherapp.R.string.percent)
    val mm = stringResource(com.jasminsp.weatherapp.R.string.mm)
/*
    val windValue = 1
    val humidityValue = 60
    val rainValue = 0
    val uvValue = 0

 */
    Card(
        elevation = 30.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 300.dp, max = 330.dp)
            .padding(start = 30.dp, top = 30.dp, end = 30.dp, bottom = 15.dp)
            .shadow(elevation = 30.dp, shape = RoundedCornerShape(size = 10.dp), clip = false)
    ){
        Column() {
            Row(modifier = Modifier.fillMaxWidth(),
                ) {
                Row(modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                   .fillMaxWidth(0.6f) , verticalAlignment = Alignment.CenterVertically) {
                    Text(weekday+ "   ")
                    Icon(painter = painterResource(com.jasminsp.weatherapp.R.drawable.cloudy_icon),modifier = Modifier.requiredWidth(60.dp).padding(start = 1.dp), contentDescription ="" )
                }
                Column(modifier = Modifier
                    .padding(top = 20.dp, end = 20.dp)
                    .width(80.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(high + " | " + low)
                    Text(max + " | " + min)
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
                Text("1" + metersSecond, modifier = Modifier.padding(vertical = 3.dp))
                Text("60" + percent, modifier = Modifier.padding(vertical = 3.dp))
                Text("0" + mm, modifier = Modifier.padding(vertical = 3.dp))
                Text("0", modifier = Modifier.padding(vertical = 3.dp))

            }
        }
            Row(modifier = Modifier.padding(top = 10.dp, bottom = 20.dp).fillMaxWidth(),
                horizontalArrangement  =  Arrangement.Center) {
                Column(modifier = Modifier.padding(end= 10.dp)) {
                    Text(sunrise)
                    Text(sunrisetime)
                }
                Column(modifier = Modifier.padding(start= 10.dp)) {
                    Text(sunset)
                    Text(sunsettime)
                }
            }
    }}
}