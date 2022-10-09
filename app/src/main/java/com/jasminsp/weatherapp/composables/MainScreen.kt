package com.jasminsp.weatherapp.composables

import android.util.Log
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jasminsp.weatherapp.R
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

    fromDb.value?.forEach {
        viewModel.getFavouriteWeather(it.latitude, it.longitude, it.locationUid)
    }
    LazyColumn {
        item {
            favourite?.forEach { favourite ->
                Log.i("WEATHER_RESPONSE", "$favourite")
                    FavouriteCard(navController, viewModel, favourite)
            }
        }
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteCard(navController: NavController, viewModel: WeatherViewModel, favourite: WeatherApiService.MainWeather) {
    val squareSize = 150.dp
    val swipeAbleState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)


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
                .clickable { navController.navigate("detail view") }
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
                        painter = painterResource(R.drawable.helsinki_ican), contentDescription = "Helsinki" )
                    Column(Modifier.fillMaxSize().background(
                        setGradient(getWeatherCondition(favourite.current_weather.weathercode, 2) as Color)
                    )) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Column(Modifier.padding(top = 30.dp, start = 20.dp)) {
                                Text("${getCurrentTemperature(favourite)}${Units().temperature}",style = MaterialTheme.typography.h2, color = Color.White)
                                Text(favourite.name ?: "", style = MaterialTheme.typography.subtitle1, color = Color.White)
                                Text(getTimeNow(), style = MaterialTheme.typography.body1,  color = Color.White)
                            }
                            Column(Modifier.padding(top = 15.dp, end = 20.dp), horizontalAlignment = Alignment.End) {
                                Image( painter = painterResource(getWeatherCondition(1, 1) as Int), contentDescription = "")
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

