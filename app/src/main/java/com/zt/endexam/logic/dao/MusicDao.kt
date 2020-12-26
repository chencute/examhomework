package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zt.endexam.SunnyWeatherApplication


//保存音乐
object MusicDao {
    fun savemusicList(musicList:MutableList<String>) {
        sharedPreferences().edit {
            putString("musicList", Gson().toJson(musicList))
        }
    }

    fun getSavedmusicList(): MutableList<String> {
        val musicListJson = sharedPreferences().getString("musicList","")
        return Gson().fromJson(musicListJson,
            object : TypeToken<MutableList<String?>?>() {}.type
        )
    }

    fun savemusicNameList(musicNameList:MutableList<String>) {
        sharedPreferences().edit {
            putString("musicNameList", Gson().toJson(musicNameList))
        }
    }

    fun getSavedmusicNameList(): MutableList<String> {
        val musicNameListJson = sharedPreferences().getString("musicNameList","")
        return Gson().fromJson(musicNameListJson,
            object : TypeToken<MutableList<String?>?>() {}.type
        )
    }

    fun saveartisitNameList(artisitNameList:MutableList<String>) {
        sharedPreferences().edit {
            putString("artisitNameList", Gson().toJson(artisitNameList))
        }
    }

    fun getSavedartisitNameList(): MutableList<String> {
        val artisitNameListJson = sharedPreferences().getString("artisitNameList","")
        return Gson().fromJson(artisitNameListJson,
            object : TypeToken<MutableList<String?>?>() {}.type
        )
    }

    fun savecurrent(current:Int) {
        sharedPreferences().edit {
            putString("current",current.toString())
        }
    }

    fun getSavedcurrent(): Int{
        val current = sharedPreferences().getString("current","")
        return current!!.toInt()
    }

    fun ismusicSaved() = sharedPreferences().contains("musicList")

    private fun sharedPreferences() = SunnyWeatherApplication.context
        .getSharedPreferences("Music", Context.MODE_PRIVATE)
}