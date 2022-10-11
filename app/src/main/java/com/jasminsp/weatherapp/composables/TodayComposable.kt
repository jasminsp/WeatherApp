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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.R
import androidx.compose.material.Icon as Icon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TwelweHours() {
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
                .fillMaxSize(), horizontalAlignment = CenterHorizontally)

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
                }}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                ) {
                    Column( modifier = Modifier.fillMaxWidth(0.5f)) {
                        Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
                        Text(todayInfo,Modifier.align(CenterHorizontally), style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onSecondary)
                    }
                    Column( modifier = Modifier) {
                        Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
                        Text(sevendayinfo,Modifier.align(CenterHorizontally), style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
                }
            }}





