package com.zt.endexam.logic

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.zt.endexam.logic.dao.*
import com.zt.endexam.logic.model.GameModel.CardMatchingGame
import com.zt.endexam.logic.model.GameModel.Score
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

    //保存地域
    fun savePlace(place:Location) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    //保存音乐数据
    fun savemusicList(musicList:MutableList<String>) = MusicDao.savemusicList(musicList)

    fun getSavedmusicList(): MutableList<String> = MusicDao.getSavedmusicList()

    fun ismusicSaved() = MusicDao.ismusicSaved()

    fun savemusicNameList(musicNameList:MutableList<String>) = MusicDao.savemusicNameList(musicNameList)

    fun getSavedmusicNameList(): MutableList<String> = MusicDao.getSavedmusicNameList()

    fun saveartisitNameList(artisitNameList:MutableList<String>) = MusicDao.saveartisitNameList(artisitNameList)

    fun getSavedartisitNameList(): MutableList<String> = MusicDao.getSavedartisitNameList()

    fun savecurrent(current:Int) = MusicDao.savecurrent(current)

    fun getSavedcurrent(): Int = MusicDao.getSavedcurrent()

    //保存当前页面数据
    fun saveDataStatus(DataStatus:Int)  = dataDao.saveDataStatus(DataStatus)

    fun getDataStatus(): Int = dataDao.getDataStatus()

    fun isDataStatusSaved() = dataDao.isDataStatusSaved()

    //保存游戏玩家得分
    fun saveScore(scoreList: MutableList<Score>) = scoreDao.saveScore(scoreList)

    fun getSavedScore(): MutableList<Score> = scoreDao.getSavedScore()

    fun isScoreSaved() = scoreDao.isScoreSaved()

    //保存游戏状态
    fun saveGameStatus(GameStatus:Int) = gameStatusDao.saveGameStatus(GameStatus)

    fun getGameStatus(): Int = gameStatusDao.getGameStatus()

    fun isGameStatusSaved() = gameStatusDao.isGameStatusSaved()

    //保存游戏难度
    fun saveSetGameStatus(SetGameStatus:Int) = setGamestatusDao.saveSetGameStatus(SetGameStatus)

    fun getSetGameStatus(): Int = setGamestatusDao.getSetGameStatus()

    fun isSetGameStatusSaved() = setGamestatusDao.isSetGameStatusSaved()

    //保存横竖屏状态
    fun saveCurrentStatus(CurrentStatus:Boolean) = CurrentStatusDao.saveCurrentStatus(CurrentStatus)

    fun getCurrentStatus(): Boolean = CurrentStatusDao.getCurrentStatus()

    fun isCurrentStatusSaved() = CurrentStatusDao.isCurrentStatusSaved()

    //保存游戏数据
    fun saveGameData(game: CardMatchingGame) = GameDataDao.saveGameData(game)

    fun getSavedGameData(): CardMatchingGame = GameDataDao.getSavedGameData()

    fun isGameDataSaved() = GameDataDao.isGameDataSaved()

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