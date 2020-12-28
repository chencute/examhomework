package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.model.GameModel.CardMatchingGame
import com.zt.endexam.logic.model.Location

/**
 * 保存游戏数据
 */
object GameDataDao {

    fun saveGameData(game: CardMatchingGame) {
        sharedPreferences().edit {
            putString("game", Gson().toJson(game))
        }
    }

    fun getSavedGameData(): CardMatchingGame {
        val gameJson = sharedPreferences().getString("game","")
        return Gson().fromJson(gameJson, CardMatchingGame::class.java)
    }

    fun isGameDataSaved() = sharedPreferences().contains("game")

    private fun sharedPreferences() = SunnyWeatherApplication.context
            .getSharedPreferences("gamedata", Context.MODE_PRIVATE)
}