package com.zt.endexam.logic.network

import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.model.DailyResponse
import com.zt.endexam.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//访问天气信息接口
interface WeatherService {
    //访问实时数据 location为城市ID
    @GET("v7/weather/now?key=${SunnyWeatherApplication.key}")
    fun getRealtimeWeather(@Query("location")location:String):Call<RealtimeResponse>

    //访问未来7天 天气信息 location为城市ID
    @GET("v7/weather/3d?key=${SunnyWeatherApplication.key}")
    fun getDailyWeather(@Query("location")location:String):Call<DailyResponse>
}