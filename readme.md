### Forecast - weather application

This project was part of Metropolia University of Applied sciences Kotlin and Compose development course where one of the main topics were
also Bluetooth, maps, Android sensors and AR.

## Table of Content

- Overview
- App features
- Libraries
- Back-end and API service
- Set up
- Screenshots
- Contributors

## Overview

Forecast is a simple weather application which gives two type of data and visualizes them on the screen. Data that is shown
is taken from external API services as well as phone's own sensors. 

## Features:

- No need for login, instant weather data visible
- Add favourites for the locations that interest you
- See weather daily and hourly for up to seven days
- Get information about your near surroundings with the help of your phone sensors
- Get notifications on weather conditions

## Libraries:

- Jetpack Compose - [Android Jetpack Compose](https://developer.android.com/jetpack)
- Room database with [sqLite](https://www.sqlite.org/index.html)
- [material design](https://material.io/)

## Backend and APIs service:

Forecast relies on Room database fo storing favourite locations. On top of this it uses to API services for the weather data. 
Data is updated every two hours automatically from [Open-Meteo-api](https://open-meteo.com/en) and it's [geocoding-api](https://open-meteo.com/en/docs/geocoding-api).


## Set up

To run the application, you need to git clone the repo and run on android studio

```
$ git clone https://github.com/jasminsp/WeatherApp.git

```

## Screenshots

Some of the views


## Contributors

[Matti Stenvall](https://github.com/stenvma81)

[Jasmin Partanen](https://github.com/jasminsp)

[Maiju Himberg](https://github.com/maijuhimberg)
