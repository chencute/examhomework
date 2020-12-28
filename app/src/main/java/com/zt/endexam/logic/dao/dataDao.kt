package com.zt.endexam.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.zt.endexam.SunnyWeatherApplication

/**
 * 本地存储当前页数状态
 */
object dataDao {

    fun saveDataStatus(DataStatus:Int) {
       sharedPreferences().edit {
            putString("DataStatus",DataStatus.toString() )
        }
    }

    fun getDataStatus(): Int{
        val DataStatus = sharedPreferences().getString("DataStatus","")
        return DataStatus!!.toInt()
    }

    fun isDataStatusSaved() = sharedPreferences().contains("DataStatus")

    private fun sharedPreferences() = SunnyWeatherApplication.context
        .getSharedPreferences("data", Context.MODE_PRIVATE)
}