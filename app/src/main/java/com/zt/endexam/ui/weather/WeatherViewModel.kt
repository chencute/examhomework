package com.zt.endexam.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.Location

class WeatherViewModel :ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationID = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) {location ->
        Repository.refreshWeather(location.id)
    }

    fun refreshWeather(location:Location) {
        locationLiveData.value = location
    }
}