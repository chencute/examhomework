package com.zt.endexam.ui.game

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.MainActivity
import com.zt.endexam.R
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.GameModel.CardMatchingGame
import com.zt.endexam.logic.model.GameModel.Score
import com.zt.endexam.logic.model.tranGamesetStatus
import com.zt.endexam.ui.music.MusicService
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.util.*

class GameActivity : AppCompatActivity() , ServiceConnection {

    companion object {
        var cardCount = when(Repository.getSetGameStatus()) {
            1 -> 8
            2 -> 16
            3 -> 24
            else -> 8
        }
        var game: CardMatchingGame  = CardMatchingGame(cardCount)
    }

    var currentStatus = false

    lateinit var adapter:CardAdapter

    lateinit var scoreList: MutableList<Score>

    lateinit var  rgame : CardMatchingGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //重新设置难度
        if(cardCount != tranGamesetStatus(Repository.getSetGameStatus()).setGameStatus) {
            cardCount = tranGamesetStatus(Repository.getSetGameStatus()).setGameStatus
            game.reset(cardCount)
        }

        //获取本地数据
        if(Repository.isGameDataSaved()) {
            rgame = Repository.getSavedGameData()
        }

        //判断是否用户已给权限
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没给 即申请权限
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        } else {
            startMusicService()
        }

        //        创建网格式布局布局
        val layoutManager: GridLayoutManager
        //        获取到手机的配置文件来判断当前所处的手机状态
        val configuration = resources.configuration
        var statusjj = 0
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
        //            竖屏
            layoutManager = GridLayoutManager(this,4, GridLayoutManager.VERTICAL,false)
            if(Repository.getCurrentStatus()) {
                statusjj = 1
            }
        }else {
            layoutManager = GridLayoutManager(this,6, GridLayoutManager.VERTICAL,false)
            Repository.saveCurrentStatus(true)
        }

        if(!Repository.getCurrentStatus()) {
            //对于每次游戏进行清零操作
            if (Repository.isGameStatusSaved()) {
                when(Repository.getGameStatus()) {
                    1 -> game.reset(cardCount)
                }
            }
        } else {
            if(Repository.isGameDataSaved()) {
                game = rgame
            } else {
                game = CardMatchingGame(cardCount)
            }
        }

        if(statusjj == 1) {
            Repository.saveCurrentStatus(false)
        }

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = layoutManager

        adapter = CardAdapter(game)

        recyclerview.adapter = adapter

        adapter.setOnClickListener {
            game.chooseCardAtIndex(it)
            updateUI()
        }

        updateUI()

        //退出游戏
        val exitGame = findViewById<Button>(R.id.exitGame)
        exitGame.setOnClickListener {
            stopMusicService()
            if(game.score != 0) {
                if(Repository.isScoreSaved()) {
                    scoreList = Repository.getSavedScore()
                } else {
                    scoreList = mutableListOf()
                }
                val canlendar = Calendar.getInstance()
                val year = canlendar.get(Calendar.YEAR).toString()
                val month = canlendar.get(Calendar.MONTH).toString()
                val day =  canlendar.get(Calendar.DAY_OF_MONTH).toString()
                val hour =  canlendar.get(Calendar.HOUR_OF_DAY).toString()
                val date = year + "-" + month + "-" + day + "-" + hour
                val score = Score(date, game.score)
                scoreList.add(score)
//                Log.d("Test","${scoreList}")
                Repository.saveScore(scoreList)
            }
            Repository.saveDataStatus(4)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        //在停止的时候保存数据
        Repository.saveGameData(game)
        //退出时停止音乐
        stopMusicService()
//        val  intent = Intent(this, MusicService::class.java)
//        stopService(intent)
    }


    fun updateUI() {
        adapter.notifyDataSetChanged()
    }

    //申请权限的回调函数
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onServiceDisconnected(name: ComponentName?) {

    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        //摧毁服务
        val  intent = Intent(this, MusicService::class.java)
        stopService(intent)
    }

    //开启服务
    fun startMusicService() {
        val  intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.CMOND,1)
        startService(intent)
    }

    fun stopMusicService() {
        val  intent = Intent(this, MusicService::class.java)
        intent.putExtra(MusicService.CMOND,2)
        startService(intent)
    }
}