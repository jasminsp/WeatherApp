package com.jasminsp.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp



@Composable
fun FavouriteCard(

) {
    val temperature = stringResource(com.jasminsp.weatherapp.R.string.temperature)
    val city = stringResource(com.jasminsp.weatherapp.R.string.city)
    val time = stringResource(com.jasminsp.weatherapp.R.string.time)
    val am = stringResource(com.jasminsp.weatherapp.R.string.am)
    val pm = stringResource(com.jasminsp.weatherapp.R.string.pm)
    val forecast = stringResource(R.string.forecast_letters)
    val min = stringResource(com.jasminsp.weatherapp.R.string.min)
    val max = stringResource(com.jasminsp.weatherapp.R.string.max)


    Card(
        elevation = 30.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeightIn(min = 220.dp, max = 230.dp)
            .padding(start = 30.dp, top = 15.dp, end = 30.dp, bottom = 15.dp)
            .shadow(elevation = 30.dp, shape = RoundedCornerShape(size = 10.dp), clip = false)
    ) {
        Image(modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.helsinki_ican), contentDescription = "Helsinki" )
        Image(modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.yellowlinear), contentDescription = "")

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement  =  Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier.padding(top = 30.dp, start = 20.dp)) {
                Text(temperature,style = MaterialTheme.typography.h2, color = Color.White)
                Text(city, style = MaterialTheme.typography.subtitle1, color = Color.White)
                Text(time + am, style = MaterialTheme.typography.body1,  color = Color.White)
                Text(min + " / " + max, style = MaterialTheme.typography.body2, color = Color.White)
            }


            Column(modifier = Modifier.padding(top = 15.dp, end = 20.dp),) {
                Image(
                    painter = painterResource(R.drawable.sunny), contentDescription = "")
                Text(forecast, style = MaterialTheme.typography.subtitle2, color = Color.White)
            }
        }
    }}


   


