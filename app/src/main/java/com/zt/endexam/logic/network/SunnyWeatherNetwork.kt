package com.zt.endexam.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 统一的网络数据源访问入口 对所有的网络请求的API进行封装
 * 采用协程技术
 */
object SunnyWeatherNetwork {

    //创建PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create(PlaceService::class.java)
    //调用PlaceService接口中定义的serachPlaces()方法 声明为挂起函数
    suspend fun searchPlaces(location:String) = placeService.serachPlaces(location).await()

    //自动处理返回
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("resopnse body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}