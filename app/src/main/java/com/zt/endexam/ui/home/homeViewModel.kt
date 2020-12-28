package com.zt.endexam.ui.home

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class homeViewModel:ViewModel() {

    val currentDate : LiveData<String>
                    get() = _currentDate

    private val _currentDate = MutableLiveData<String>()

    init {
        currentDateB()
    }

    //获取当前年-月-日
   private fun currentDateB() {
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val calendar = Calendar.getInstance()
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH] + 1
                val day = calendar[Calendar.DAY_OF_MONTH]
                //24小时制
                val hour = calendar[Calendar.HOUR_OF_DAY]
                val minute = calendar[Calendar.MINUTE]
                val second = calendar[Calendar.SECOND]
                _currentDate.value =  year.toString() + "年" + month.toString() + "月" + day.toString() + "日  " + hour.toString() + ":" + minute.toString() + ":" + second.toString()
                handler.postDelayed(this,1000)
            }
        }
        handler.post(runnable)
    }
}