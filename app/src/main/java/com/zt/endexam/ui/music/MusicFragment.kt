package com.zt.endexam.ui.music

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothClass
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.zt.endexam.MainActivity
import com.zt.endexam.R
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.getMusicImge
import org.w3c.dom.Text
import java.security.Provider
import kotlin.concurrent.thread


class MusicFragment : Fragment(), ServiceConnection {

    companion object {
        fun newInstance() = MusicFragment()
    }

    var binder:MusicService.MusicBinder ?= null

    var isPauseStatus = true

    lateinit var seekBar:SeekBar

    lateinit var musicName:TextView

    lateinit var artistName:TextView

    lateinit var musicImage:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //判断是否用户已给权限
        if(ContextCompat.checkSelfPermission(SunnyWeatherApplication.context,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //没给 即申请权限
            requestPermissions( arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }

         seekBar = view.findViewById(R.id.seekBar)
         musicName = view.findViewById(R.id.musicName)
         artistName = view.findViewById(R.id.artistName)
         musicImage = view.findViewById(R.id.musicImage)

        //设置拖动进度条获取音乐变化监听
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //如果是用户点击，改变音乐进度
                if(fromUser) {
                    binder?.currentPosition = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //如果不去除TODO注释 就会抛出NotImplementedErro以致于每次执行拖动会报错
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val btnPlay = view.findViewById<Button>(R.id.btnPlay)
        btnPlay.setOnClickListener {
            //若为暂停 改变状态为播放
            if(isPauseStatus) {
                isPauseStatus = false
                btnPlay.setBackgroundResource(R.drawable.stop_play)
                startMusicService()
                onPlay()
            } else {
                isPauseStatus = true
                btnPlay.setBackgroundResource(R.drawable.start_play)
                onpause()
            }
        }

        val btnPrev = view.findViewById<Button>(R.id.btnPrev)
        btnPrev.setOnClickListener {
            if(isPauseStatus) {
                btnPlay.setBackgroundResource(R.drawable.stop_play)
                isPauseStatus = false
            }
            onPrev()
        }

        val btnNext = view.findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            if(isPauseStatus) {
                btnPlay.setBackgroundResource(R.drawable.stop_play)
                isPauseStatus = false
            }
            onNext()
        }

        val btnList = view.findViewById<Button>(R.id.btnList)
        val drawerLayout = view.findViewById<DrawerLayout>(R.id.musicdrawerLayout)
        btnList.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

//        drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener {
//            override fun onDrawerStateChanged(newState: Int) {}
//
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
//
//            override fun onDrawerClosed(drawerView: View) {
//                val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//            }
//
//            override fun onDrawerOpened(drawerView: View) {}
//
//        })

    }

    fun onPlay() {
        val  intent = Intent(context,MusicService::class.java)
        intent.putExtra(MusicService.CMOND,1)
        activity?.startService(intent)
    }

    fun onpause() {
        val  intent = Intent(context,MusicService::class.java)
        intent.putExtra(MusicService.CMOND,2);
        activity?.startService(intent)
    }

    fun onNext() {
        val  intent = Intent(context,MusicService::class.java)
        intent.putExtra(MusicService.CMOND,3)
        activity?.startService(intent)
    }

    fun onPrev() {
        val  intent = Intent(context,MusicService::class.java)
        intent.putExtra(MusicService.CMOND,4)
        activity?.startService(intent)
    }

    fun onstop() {
        val  intent = Intent(context,MusicService::class.java)
        intent.putExtra(MusicService.CMOND,5)
        activity?.startService(intent)
        activity?.bindService(intent,this, Context.BIND_AUTO_CREATE)
    }

    //开启服务
    fun startMusicService() {
        val  intent = Intent(context,MusicService::class.java)
        //开启时为暂停状态
        intent.putExtra(MusicService.CMOND,5)
        activity?.startService(intent)
        activity?.bindService(intent,this, Context.BIND_AUTO_CREATE)
    }

    //申请权限的回调函数
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onDestroy() {
        super.onDestroy()
        //摧毁服务
        val  intent = Intent(context,MusicService::class.java)
        activity?.stopService(intent)
        activity?.unbindService(this)
    }

    //断开连接之后调用
    override fun onServiceDisconnected(name: ComponentName?) {
        binder = null
    }

    //绑定连接成功之后调用
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        //获取到服务传来的数据
        binder = service as MusicService.MusicBinder
        //启动线程获取音乐进度事件
        thread {
            while (binder != null) {
                Thread.sleep(1000)
                //主线程
                activity?.runOnUiThread {
                    //设置进度条的最大值
                    seekBar.max = binder!!.duration
                    //设置进度条当前位置
                    seekBar.progress = binder!!.currentPosition
                    //音乐名
                    musicName.text = "歌名：${binder!!.musicName}"
                    //作者
                    artistName.text = "作者：${binder!!.artistName}"
                    //图片
                    musicImage.setImageResource(getMusicImge(binder!!.musicName).bg)
                }
            }
        }
    }

}