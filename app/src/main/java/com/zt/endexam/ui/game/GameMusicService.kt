package com.zt.endexam.ui.game

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import java.io.IOException

class GameMusicService : Service() {

    companion object {
        val CMOND = "operate"
    }

    private val TAG = "Test"
    val mediaPlayer = MediaPlayer()
    var musicList = mutableListOf<String>()
    var musicNameList = mutableListOf<String>()
    var artisitNameList = mutableListOf<String>()
    var current = 1 //当前播放第几首歌
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
        current = 1
        Log.d("Test","jiji")
        this.getMusicList()
        //回调 对应于mediaplayer.prepareaysnc()
        mediaPlayer.setOnPreparedListener{
            it.start()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //通过intent获得mainActivity传递的参数值,由于只能获得个别参数，因此还需要采用bind来获取更多参数
        val vari = intent?.getIntExtra(CMOND,2)
        Log.d("test","jjjj=${vari}")
        when(vari) {
            1 -> play()
            2 -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
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