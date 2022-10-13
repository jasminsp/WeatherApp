package com.jasminsp.weatherapp.composables

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jasminsp.weatherapp.MainActivity
import com.jasminsp.weatherapp.R
import com.jasminsp.weatherapp.sensor.SensorViewModel
import com.jasminsp.weatherapp.utils.*
import com.jasminsp.weatherapp.utils.helpers.*
import com.jasminsp.weatherapp.weather.WeatherViewModel
import com.jasminsp.weatherapp.web.WeatherApiService
import kotlin.math.roundToInt

@Composable
fun SearchBar(viewModel: WeatherViewModel) {
    var searchInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(horizontal = 60.dp, vertical = 20.dp)
    ) {
        TextField(value = searchInput, onValueChange = {
            searchInput = it
            viewModel.getLocations(it)
        }, leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.large,
            placeholder = { context.getString(R.string.placeholderSearch) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(size = 10.dp),
                    clip = false
                )
        )
    }
}

@Composable
fun ShowFavourites(navController: NavController, viewModel: WeatherViewModel) {
    val favourite by viewModel.favouriteLocations.observeAsState()
    val fromDb = viewModel.favouritesFromDb.observeAsState()

    if (fromDb.value?.isNotEmpty() == true) {
        viewModel.getAllWeather()
    }
    if (favourite?.isNotEmpty() == true) {
        LazyColumn {
            item {
                favourite?.forEach { favourite ->
                    Log.i("WEATHER_RESPONSE", "$favourite")
                    FavouriteCard(navController, viewModel, favourite)
                }
            }
        }
    } else {
        Text("No favourites yet!")
    }
}

@Composable
fun ShowSearchResult(navController: NavController, viewModel: WeatherViewModel) {
    val searchResult by viewModel.searchedLocations.observeAsState()

    Column(Modifier.fillMaxWidth()) {
        // Place for search input
        LazyColumn(Modifier.fillMaxWidth()) {
            searchResult?.results?.forEach {
                item {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                addFavourite(viewModel, it.id, it.latitude, it.longitude)
                                navController.navigate("main view")
                            }) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp),
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically) {
                            Column(Modifier.padding(all = 20.dp)) {
                                Text("${it.admin3 ?: it.admin2}")
                                Text("${it.country_code}, ${it.country}",
                                    fontWeight = FontWeight(400), color = Color.Gray
                                )
                            }
                            IconButton(
                                onClick = {
                                    addFavourite(viewModel, it.id,  it.latitude, it.longitude)
                                    navController.navigate("main view")
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Delete",
                                    tint = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YourLocationCard(sensorViewModel: SensorViewModel) {
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

    var showRuuviData = remember { mutableStateOf(true) }

    val tempData = sensorViewModel.tempData.observeAsState()
    val humData = sensorViewModel.humData.observeAsState()
    val presData = sensorViewModel.presData.observeAsState()

    val tempRuuvi = sensorViewModel.tempDataTag.observeAsState()
    val humRuuvi = sensorViewModel.humDataTag.observeAsState()
    val presRuuvi = sensorViewModel.presDataTag.observeAsState()

    val dewPoint = sensorViewModel.calculateDewPoint(showRuuviData.value)

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
            Box(modifier = Modifier
                .fillMaxSize()
                //Placehoder color for the back. Delete after photos added
                .background(color = MaterialTheme.colors.primaryVariant)
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
                            )
                        )
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
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
                        Column(modifier = Modifier
                            .fillMaxSize()
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
                                    verticalArrangement = Arrangement.spacedBy(15.dp),
                                    horizontalAlignment = Alignment.End)
                                {
                                    if (showRuuviData.value) {
                                        Text(
                                            if (tempRuuvi.value!=null) String.format("%.1f", tempRuuvi.value) + "\u00B0" else "N/A",
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                        Text(
                                            if (humRuuvi.value!=null) String.format("%.0f", humRuuvi.value) + percent else "N/A",
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                        Text(
                                            String.format("%.0f", dewPoint) + "°",
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                        Text(
                                            if (presRuuvi.value!=null) String.format("%.0f", presRuuvi.value!! /100f) + airpressurehPa else "N/A",
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                    } else {
                                        Text(
                                            String.format("%.0f", tempData.value) + "°",
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                        Text(
                                            String.format("%.0f", humData.value) + percent,
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                        Text(
                                            String.format("%.0f", dewPoint) + "\u00B0",
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                        Text(
                                            String.format("%.0f", presData.value) + airpressurehPa,
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )
                                    }
                                    Button(onClick = { showRuuviData.value = !showRuuviData.value
                                    Log.d("DMG", "RuuviData.value")}) {
                                        Text("Test")
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteCard(navController: NavController, viewModel: WeatherViewModel, favourite: WeatherApiService.MainWeather) {
    val squareSize = 150.dp
    val swipeAbleState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    Log.i("id:", "${favourite.id}")

    Card(modifier = Modifier
        .padding(horizontal = 30.dp, vertical = 5.dp)
        .clip(RoundedCornerShape(15.dp))) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Red)
                .clickable { navController.navigate("detail view/${favourite.id}") }
                .swipeable(
                    state = swipeAbleState,
                    anchors = anchors,
                    thresholds = { _, _ ->
                        FractionalThreshold(0.3f)
                    },
                    orientation = Orientation.Horizontal
                )) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .padding(40.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewModel.deleteFavourite(favourite.id)
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White)) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Black)
                    }
                }
                Box(modifier = Modifier
                    .offset {
                        IntOffset(
                            swipeAbleState.offset.value.roundToInt(), 0
                        )
                    }
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .height(190.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)) {

                    Image(modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(getWeatherCondition(favourite.current_weather.weathercode, 2) as Int), contentDescription = "Helsinki" )
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(
                                setGradient(Color.LightGray)
                            )) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Column(Modifier.padding(top = 30.dp, start = 20.dp)) {
                                Text("${getCurrentTemperature(favourite)}${Units().temperature}",style = MaterialTheme.typography.h2, color = Color.White)
                                Text(favourite.name ?: "", style = MaterialTheme.typography.subtitle1, color = Color.White)
                                Text(getTimeNow(), style = MaterialTheme.typography.body1,  color = Color.White)
                            }
                            Column(Modifier.padding(top = 15.dp, end = 20.dp), horizontalAlignment = Alignment.End) {
                                Image( painter = painterResource(getWeatherCondition(favourite.current_weather.weathercode, 1) as Int), contentDescription = "")
                                Text("${getWeatherCondition(favourite.current_weather.weathercode, 0)}", style = MaterialTheme.typography.body1, color = Color.White)
                                Text("${getMinMaxTempToday(favourite, true)}${Units().temperatureShort} | ${getMinMaxTempToday(favourite, false)}${Units().temperatureShort}", style = MaterialTheme.typography.body2, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

