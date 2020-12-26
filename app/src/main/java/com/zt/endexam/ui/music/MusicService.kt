package com.zt.endexam.ui.music

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.zt.endexam.logic.Repository
import java.io.IOException

class MusicService : Service() {

    companion object {
        val CMOND = "operate"
    }
    private val TAG = "Test"
    val mediaPlayer = MediaPlayer()
    var musicList = mutableListOf<String>()
    var musicNameList = mutableListOf<String>()
    var artisitNameList = mutableListOf<String>()
    var current = 0 //当前播放第几首歌
//    var isPause = false//是否暂停 默认为暂停
    val binder = MusicBinder()

    //提供向外接口，与mainActivity通信
    inner class MusicBinder(): Binder() {
        val musicName
            get() = musicNameList.get(current)
        val duration
            get() = mediaPlayer.duration
        var currentPosition
            get() = mediaPlayer.currentPosition
            set(value) = mediaPlayer.seekTo(value)
        val size
            get() = musicList.size
        val currentIndex
            get() = current + 1
        val artistName
            get() = artisitNameList.get(current)
    }

    //只调用一次
    override fun onCreate() {
        super.onCreate()

        /**
         * 如果有本地数据则 直接获取，不需要再次遍历获取音乐数据
         */
        if (Repository.ismusicSaved()) {
            musicList = Repository.getSavedmusicList()
            musicNameList = Repository.getSavedmusicNameList()
            artisitNameList = Repository.getSavedartisitNameList()
            current = Repository.getSavedcurrent()
        } else {
            this.getMusicList()
        }
        //回调 对应于mediaplayer.prepareaysnc()
        mediaPlayer.setOnPreparedListener{
            it.start()
        }
        //当音乐完成 自动播放下一首歌曲
        mediaPlayer.setOnCompletionListener {
            current = (current + 1 + musicList.size)%musicList.size
            play()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //通过intent获得mainActivity传递的参数值,由于只能获得个别参数，因此还需要采用bind来获取更多参数
        val vari = intent?.getIntExtra(CMOND,2)
//        Log.d("Test","${vari}")
        when(vari) {
            1 -> play()
            2 -> pause()
            3 -> next()
            4 -> prev()
            5 -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): IBinder {
        //MainActivity与服务建立连接成功之后返回binder接口
        return binder
    }

    fun pause() {
        mediaPlayer.pause()
    }
    fun next() {
        current = (current + 1 + musicList.size)%musicList.size
        play()
    }
    fun prev() {
        current = (current - 1 + musicList.size)%musicList.size
        play()
    }
    fun stop() {
        mediaPlayer.stop()
        //停掉服务
        stopSelf()
    }

    //获取音乐列表
    private fun getMusicList() {
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null)
        if(cursor != null) {
            while (cursor.moveToNext()) {
                val musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                musicList.add(musicPath)
                val musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                musicNameList.add(musicName)
                val artisitName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                artisitNameList.add(artisitName)
                Log.d(TAG,"nn=${artisitName}    getMusicList:$musicPath name:$musicName")
            }
            Repository.savemusicList(musicList)
            Repository.savemusicNameList(musicNameList)
            Repository.saveartisitNameList(artisitNameList)
            Repository.savecurrent(current)
            cursor.close()
        }
    }
    //播放音乐
    fun play() {
        if (musicList.size == 0) {
            return
        }
        val path = musicList[current]//获取当前音乐的路径
        mediaPlayer.reset()//准备播放
        try {
            mediaPlayer.setDataSource(path)//根据路径找到相应的资源
            mediaPlayer.prepareAsync()//异步调用 音乐解码之后回调
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }
}