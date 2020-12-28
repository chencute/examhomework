package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.model.GameModel.Score

/**
 * 本地存储当前玩家得分
 */
object scoreDao {

    fun saveScore(scoreList:MutableList<Score>) {
        sharedPreferences().edit {
            putString("Score", Gson().toJson(scoreList) )
        }
    }

    fun getSavedScore(): MutableList<Score>{
        val scoreListJoson = sharedPreferences().getString("Score","")
        return Gson().fromJson(scoreListJoson,
            object : TypeToken<MutableList<Score?>?>() {}.type
        )
    }

    fun isScoreSaved() = sharedPreferences().contains("Score")

    private fun sharedPreferences() = SunnyWeatherApplication.context
        .getSharedPreferences("scoreData", Context.MODE_PRIVATE)
}