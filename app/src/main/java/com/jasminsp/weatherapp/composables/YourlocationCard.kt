package com.jasminsp.weatherapp.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.R

@Composable
fun YourLocationCard(


) {
    val temperature = stringResource(R.string.temperature)
    val city = stringResource(R.string.city)
    val yourlocation = stringResource(R.string.yourlocation)
    val time = stringResource(R.string.time)
    val am = stringResource(R.string.am)
    val pm = stringResource(R.string.pm)
    val forecaststring = stringResource(R.string.forecast_letters)
    val sensordatastring= stringResource(R.string.sensordata)

    val temp = stringResource(R.string.temp)
    val humidity = stringResource(R.string.humidity)
    val dewpoint = stringResource(R.string.dewpoint)
    val airpressure = stringResource(R.string.airpressure)
    val airpressurehPa = stringResource(R.string.airpressign)
    val percent = stringResource(R.string.percent)

    //Expansion
    var expandedState by remember { mutableStateOf(false)}
    //Arrow turn
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 0f else 180f)


    Box(){
    Card(
        elevation = 30.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 260.dp, max = 260.dp)
            .padding(start = 30.dp, top = 15.dp, end = 30.dp)
            .shadow(elevation = 30.dp, shape = RoundedCornerShape(size = 10.dp), clip = false)
    ) {
        Box(modifier = Modifier.fillMaxSize()
            //Placehoder color for the back. Delete after photos added
            .background(color= MaterialTheme.colors.primaryVariant)
        ) {
            //Delete these after photos added
           /* Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.helsinki_ican), contentDescription = "Helsinki"
            )
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.yellow80), contentDescription = ""
            )
            */

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Column(modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp))
                {
                    Text(temperature + "\u00B0", style = MaterialTheme.typography.h2, color = Color.White)
                    Text(yourlocation, style = MaterialTheme.typography.h4, color = Color.White)
                    Text(city, style = MaterialTheme.typography.subtitle1, color = Color.White)
                    Text(time + am, style = MaterialTheme.typography.body2, color = Color.White)
                    //Text(min + " / " + max, style = MaterialTheme.typography.body1, color = Color.White)
                }


                Column(modifier = Modifier
                    .padding(top = 15.dp, end = 20.dp),)
                {
                    Image(
                        painter = painterResource(R.drawable.clear_icon), contentDescription = ""
                    )
                    Text(forecaststring,
                        style = MaterialTheme
                            .typography.subtitle2, color = Color.White)
                }
            }
        }
//White part in the card
        Box() {
            Column(
                modifier = Modifier
                    .requiredHeightIn(min = 50.dp, max = 260.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = Color.White)
                    .clickable { expandedState = !expandedState }
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        ))
            ) {
                Row(modifier = Modifier.fillMaxWidth()
                    .padding(top= 10.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(sensordatastring +"   ",
                        style = MaterialTheme
                            .typography.subtitle2)
                    Icon(painter = painterResource(R.drawable.down_arrow_icon),
                        modifier = Modifier
                            .requiredSize(20.dp)
                            .rotate(rotationState), contentDescription = "")
                }
                //Expansion is that sensordata part
        if(expandedState){
        Column(modifier = Modifier.
        fillMaxSize()
            .padding(top = 20.dp, bottom = 20.dp))
        {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement  =  Arrangement.SpaceBetween)
            {
                Column(modifier = Modifier
                    .padding(start = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp))
                {
                    Text(temp , modifier = Modifier.padding(vertical = 3.dp))
                    Text(humidity, modifier = Modifier.padding(vertical = 3.dp) )
                    Text(dewpoint , modifier = Modifier.padding(vertical = 3.dp))
                    Text(airpressure, modifier = Modifier.padding(vertical = 3.dp))
                }
                Column(modifier = Modifier
                    .padding(start = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp))
                {
                    Text("- - - - - - - -", modifier = Modifier.padding(vertical = 3.dp))
                    Text("- - - - - - - -", modifier = Modifier.padding(vertical = 3.dp))
                    Text("- - - - - - - -", modifier = Modifier.padding(vertical = 3.dp))
                    Text("- - - - - - - -", modifier = Modifier.padding(vertical = 3.dp))
                }

                Column(modifier = Modifier
                    .padding( end = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp))
                {
                    Text("25"+ "\u00B0", modifier = Modifier.padding(vertical = 3.dp))
                    Text("55" + percent, modifier = Modifier.padding(vertical = 3.dp))
                    Text( "10.7"+"\u00B0", modifier = Modifier.padding(vertical = 3.dp))
                    Text("1013"+ airpressurehPa, modifier = Modifier.padding(vertical = 3.dp))

                }
            }

        }
    }
}
                }
        }
    }
    }


