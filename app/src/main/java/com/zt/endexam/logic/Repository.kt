package com.zt.endexam.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.zt.endexam.logic.dao.PlaceDao
import com.zt.endexam.logic.model.Location
import com.zt.endexam.logic.model.Weather
import com.zt.endexam.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层：本地没有缓存数据的情况下就去网络层获取，如果本地已经有缓存了，就直接将缓存数据返回。
 * 每次都去网络请求获取最新的数据
 */
object Repository {
    //为了能将异步获取的数据已响应式变成通知上一层，返回liveData对象
    //指定为Dispatchers.IO 使代码运行在子线程中

    fun savePlace(place:Location) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    /**
     * 获取城市信息
     */
    fun serachPlaces(location:String) = fire(Dispatchers.IO) {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(location)
            if(placeResponse.code == "200") {
                val locations = placeResponse.location
                Log.d("Test","${placeResponse}")
                Result.success(locations)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.code}"))
            }
    }

    /**
     * 获取天气信息
     */
    fun refreshWeather(location: String) = fire(Dispatchers.IO) {
            //携程作用域
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(location)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(location)
                }
                val realtimeResponse = deferredRealtime.await()
                Log.d("Test","realtimeResponse  ${realtimeResponse}")
                val dailyResponse = deferredDaily.await()
                Log.d("Test","dailyResponse  ${dailyResponse}")
                if(realtimeResponse.code == "200" && dailyResponse.code == "200") {
                    val weather = Weather(realtimeResponse.now,dailyResponse.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                            RuntimeException(
                                    "realtime response code is ${realtimeResponse.code}" +
                                            "daily response code is ${dailyResponse.code}"
                            )
                    )
                }
            }
    }

    /**
     * 统一抛异常函数
     */
    private fun <T> fire(context:CoroutineContext,block:suspend() -> Result<T>) =
            liveData<Result<T>>(context) {
                val result = try {
                    block()
                } catch (e:Exception) {
                    Result.failure<T>(e)
                }
                //类似于livedata的setValue通知数据变化
                emit(result)
            }
}