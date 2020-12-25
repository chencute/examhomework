package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.model.Location

/**
 * 本地存储城市数据
 */
object PlaceDao {
    fun savePlace(place:Location) {
        sharedPreferences().edit {
            putString("place",Gson().toJson(place))
        }
    }

    fun getSavedPlace():Location {
        val placeJson = sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Location::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context
            .getSharedPreferences("sunny_weahter",Context.MODE_PRIVATE)
}