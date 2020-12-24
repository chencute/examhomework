package com.zt.endexam.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.zt.endexam.logic.model.Location
import com.zt.endexam.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

/**
 * 仓库层：本地没有缓存数据的情况下就去网络层获取，如果本地已经有缓存了，就直接将缓存数据返回。
 * 每次都去网络请求获取最新的数据
 */
object Repository {
    //为了能将异步获取的数据已响应式变成通知上一层，返回liveData对象
    //指定为Dispatchers.IO 使代码运行在子线程中
    fun serachPlaces(location:String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(location)
            if(placeResponse.code == "200") {
                val locations = placeResponse.location
                Log.d("Test","${placeResponse}")
                Result.success(locations)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.code}"))
            }
        }catch (e:Exception) {
            Result.failure<List<Location>>(e)
        }
        //类似于livedata的setValue通知数据变化
        emit(result)
    }
}