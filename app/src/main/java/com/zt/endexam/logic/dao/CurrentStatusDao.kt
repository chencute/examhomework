package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.zt.endexam.SunnyWeatherApplication

/**
 * 保存当前所处哪种环境 横屏为true
 */
object CurrentStatusDao {

    fun saveCurrentStatus(CurrentStatus:Boolean) {
        sharedPreferences().edit {
            putString("CurrentStatus",CurrentStatus.toString() )
        }
    }

    fun getCurrentStatus(): Boolean{
        val CurrentStatus = sharedPreferences().getString("CurrentStatus","")
        return CurrentStatus!!.toBoolean()
    }

    fun isCurrentStatusSaved() = sharedPreferences().contains("CurrentStatus")

    private fun sharedPreferences() = SunnyWeatherApplication.context
            .getSharedPreferences("currentstatus", Context.MODE_PRIVATE)
}