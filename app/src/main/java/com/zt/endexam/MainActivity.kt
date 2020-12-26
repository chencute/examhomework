package com.zt.endexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zt.endexam.ui.music.MusicFragment
import com.zt.endexam.ui.place.PlaceFragment

class MainActivity : AppCompatActivity() {

    val forecastFragment = PlaceFragment.newInstance()

    val musicFragment = MusicFragment.newInstance()

    val musicListFragment = com.zt.endexam.ui.music.musicListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        //点击底部导航栏事件
        bottomNav.setOnNavigationItemReselectedListener{
            when(it.itemId) {
                R.id.navigation_forecast ->
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout_box,forecastFragment)
                            .commit()
                R.id.navigation_music ->
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout_box,musicFragment)
                            .commit()
                R.id.navigation_game ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,musicListFragment)
                        .commit()
            }
            true
        }
    }
}