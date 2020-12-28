package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.zt.endexam.SunnyWeatherApplication


/**
 * 当前游戏状态 1 开始游戏 再来一局  2 继续游戏
 */
object gameStatusDao {

    fun saveGameStatus(GameStatus:Int) {
        sharedPreferences().edit {
            putString("GameStatus",GameStatus.toString() )
        }
    }

    fun getGameStatus(): Int{
        val GameStatus = sharedPreferences().getString("GameStatus","")
        return GameStatus!!.toInt()
    }

    fun isGameStatusSaved() = sharedPreferences().contains("GameStatus")

    private fun sharedPreferences() = SunnyWeatherApplication.context
        .getSharedPreferences("gameStatus", Context.MODE_PRIVATE)
}