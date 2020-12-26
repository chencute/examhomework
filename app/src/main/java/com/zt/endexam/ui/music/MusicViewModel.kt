package com.zt.endexam.ui.music


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.dao.MusicDao
import com.zt.endexam.logic.model.MusicData

class MusicViewModel :ViewModel() {

    val musicDatas = ArrayList<MusicData>()

    var musicStatus = MutableLiveData<Boolean>()

    fun savemusicList(musicList:MutableList<String>) = MusicDao.savemusicList(musicList)

    fun getSavedmusicList(): MutableList<String> = MusicDao.getSavedmusicList()

    fun ismusicSaved() = MusicDao.ismusicSaved()

    fun savemusicNameList(musicNameList:MutableList<String>) = MusicDao.savemusicNameList(musicNameList)

    fun getSavedmusicNameList(): MutableList<String> = MusicDao.getSavedmusicNameList()

    fun saveartisitNameList(artisitNameList:MutableList<String>) = MusicDao.saveartisitNameList(artisitNameList)

    fun getSavedartisitNameList(): MutableList<String> = MusicDao.getSavedartisitNameList()

    fun savecurrent(current:Int) = MusicDao.savecurrent(current)

    fun getSavedcurrent(): Int = MusicDao.getSavedcurrent()

    //获取音乐信息子项
    fun getMusicData():List<MusicData> {
        val musicNameList = getSavedmusicNameList()
        val artisitNameList = getSavedartisitNameList()
        val musicSize = musicNameList.size

        for (i in 0 until musicSize) {
            val musicData = MusicData(musicNameList[i],artisitNameList[i],i)
            musicDatas.add(musicData)
        }
        return musicDatas
    }

}