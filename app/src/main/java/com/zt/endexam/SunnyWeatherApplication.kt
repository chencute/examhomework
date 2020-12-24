package com.zt.endexam

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 建立全局获取context方法
 */
class SunnyWeatherApplication :Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //固定令牌
        const val key = "61963bd3df524fad9ddd8935ea3348b1"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}