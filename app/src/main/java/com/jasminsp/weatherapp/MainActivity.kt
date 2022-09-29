package com.jasminsp.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasminsp.weatherapp.ui.theme.WeatherAppTheme
import com.jasminsp.weatherapp.weather.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Console

class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var weatherViewModel: WeatherViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            weatherViewModel = WeatherViewModel(application)
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GetLocations(weatherViewModel)
                }
            }
        }
    }

    @Composable
    fun GetLocations(viewModel: WeatherViewModel) {
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
                                    onSearchResultClick(viewModel, it.latitude, it.longitude)
                                }) {
                            Column(Modifier.padding(all = 20.dp)) {
                                Row(Modifier.fillMaxWidth().padding(start = 15.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
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
}

// Get data of the location clicked
private fun onSearchResultClick(viewModel: WeatherViewModel, lat: Double, long: Double) {
    viewModel.getFavouriteWeather(lat, long)
    viewModel.addFavourite(lat, long)
}