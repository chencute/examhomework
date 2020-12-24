package com.zt.endexam.logic.network

import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.model.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 网络请求接口定义处
 */
interface PlaceService {
    /**
     * 根据城市名进行数据的请求
     * 请求参数location 城市名
     * 返回：PlaceResponse数据结构
     */
    @GET("v2/city/lookup?key=${SunnyWeatherApplication.key}")
    fun serachPlaces(@Query("location")location: String): retrofit2.Call<PlaceResponse>
}