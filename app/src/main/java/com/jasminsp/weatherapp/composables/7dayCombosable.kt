package com.jasminsp.weatherapp.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SevenDays() {

    val city = stringResource(R.string.city)
    val bigtemperature = 27
    val today = stringResource(R.string.today)
    val yourlocation = stringResource(R.string.yourlocation)
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
//navigation arrow
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
                Icon(
                    painter = painterResource(R.drawable.nt_clear),
                    contentDescription = "",
                    tint = Color.White
                )
                Text(
                    bigtemperature.toString() + "\u00B0",
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



