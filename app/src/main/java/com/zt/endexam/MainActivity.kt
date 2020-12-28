package com.zt.endexam

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zt.endexam.logic.Repository
import com.zt.endexam.ui.game.GameInterfaceFragment
import com.zt.endexam.ui.game.dialogScore
import com.zt.endexam.ui.music.MusicFragment
import com.zt.endexam.ui.place.PlaceFragment

class MainActivity : AppCompatActivity() {

    val homeFragment = com.zt.endexam.ui.home.homeFragment.newInstance()

    val forecastFragment = PlaceFragment.newInstance()

    val musicFragment = MusicFragment.newInstance()

    val gameInterfaceFragment = GameInterfaceFragment.newInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        if(Repository.isDataStatusSaved() && Repository.getDataStatus() != 0) {
            when(Repository.getDataStatus()) {
                1 -> {
                    bottomNav.selectedItemId = R.id.navigation_home
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,homeFragment)
                        .commit()
                }
                2 ->
                {
                    bottomNav.selectedItemId = R.id.navigation_music
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,musicFragment)
                        .commit()
                }
                3 -> {
                    bottomNav.selectedItemId = R.id.navigation_forecast
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,forecastFragment)
                        .commit()
                }
                4 -> {
                    bottomNav.selectedItemId = R.id.navigation_game
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,gameInterfaceFragment)
                        .commit()
                }
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_box,homeFragment)
                .commit()
        }

        Repository.saveDataStatus(0)

        //点击底部导航栏事件
        bottomNav.setOnNavigationItemSelectedListener{
            when(it.itemId) {
                R.id.navigation_home ->
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_box,homeFragment)
                        .commit()
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
                        .replace(R.id.frameLayout_box,gameInterfaceFragment)
                        .commit()
            }
            true
        }
    }
}