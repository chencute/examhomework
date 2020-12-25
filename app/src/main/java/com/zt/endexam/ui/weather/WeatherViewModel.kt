package com.zt.endexam.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.Location
import com.zt.endexam.logic.model.cityID

class WeatherViewModel :ViewModel() {
    private val locationLiveData = MutableLiveData<cityID>()

    var cityid = ""

    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) {location ->
        Repository.refreshWeather(location.id)
    }

    fun refreshWeather(cityid:String) {
        locationLiveData.value = cityID(cityid)
    }
}