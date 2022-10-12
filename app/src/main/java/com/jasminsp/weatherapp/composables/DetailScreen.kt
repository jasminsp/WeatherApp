package com.jasminsp.weatherapp.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.R
import com.jasminsp.weatherapp.utils.Units
import com.jasminsp.weatherapp.utils.helpers.*
import com.jasminsp.weatherapp.weather.WeatherViewModel

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
                    Icon(painter = painterResource(getWeatherCondition(
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