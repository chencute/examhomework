package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.zt.endexam.SunnyWeatherApplication


/**
 * 保存游戏难易程度 默认1 简单（8） 2 难（16） 3 非常难（24）
 */
object setGamestatusDao {

    fun saveSetGameStatus(SetGameStatus:Int) {
        sharedPreferences().edit {
            putString("SetGameStatus",SetGameStatus.toString() )
        }
    }

    fun getSetGameStatus(): Int{
        val SetGameStatus = sharedPreferences().getString("SetGameStatus","")
        return SetGameStatus!!.toInt()
    }

    fun isSetGameStatusSaved() = sharedPreferences().contains("SetGameStatus")

    private fun sharedPreferences() = SunnyWeatherApplication.context
            .getSharedPreferences("setgameStatus", Context.MODE_PRIVATE)
}