package com.jasminsp.weatherapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasminsp.weatherapp.weather.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun ShowFavourites(viewModel: WeatherViewModel) {
    val favouritesFromDb by viewModel.getFavourites().observeAsState()
    val favourite by viewModel.favouriteLocations.observeAsState()
    val scope = rememberCoroutineScope()

    LazyColumn {
        favouritesFromDb?.forEach {
            scope.launch {
                viewModel.getFavouriteWeather(it.latitude, it.longitude)
            }
        }
        item {
            favourite?.forEach {
                Text("Temperature is: ${it.current_weather.temperature}")
            }
        }
    }
}


@Composable
fun SearchLocations(viewModel: WeatherViewModel) {
    val searchInput by remember { mutableStateOf("berlin") }
    viewModel.getLocations(searchInput)
    ShowSearchResult(viewModel)
}

@Composable
fun ShowSearchResult(viewModel: WeatherViewModel) {
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
                                addFavourite(viewModel, it.latitude, it.longitude)
                            }) {
                        Column(Modifier.padding(all = 20.dp)) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                if (it.admin3 != null) Text(
                                    it.admin3,
                                    fontSize = 22.sp
                                ) else it.admin2?.let { name -> Text(name, fontSize = 22.sp) }
                                Text("Add", Modifier.padding(top = 20.dp), color = Color.Gray)
                            }
                            Text(
                                "${it.admin1}, ${it.country}",
                                Modifier.padding(start = 15.dp),
                                fontWeight = FontWeight(400), color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}