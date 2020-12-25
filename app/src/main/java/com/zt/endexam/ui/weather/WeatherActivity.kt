package com.zt.endexam.ui.weather

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zt.endexam.R
import com.zt.endexam.logic.model.Weather
import com.zt.endexam.logic.model.getSky
import com.zt.endexam.logic.model.lifeNum
import com.zt.endexam.logic.model.temperature
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        if(viewModel.cityid.isEmpty()) {
            viewModel.cityid = intent.getStringExtra("cityid") ?: ""
        }

        if(viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("placeName") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if(weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull() ?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.cityid)
    }

    private fun showWeatherInfo(weather: Weather) {

        //提取数据
        val placeName = findViewById<TextView>(R.id.placeName)
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        //填充now.xml布局中的数据
        val currentTemp = findViewById<TextView>(R.id.currentTemp)
        currentTemp.text =  "${realtime.temp.toInt()} ℃"
        val currentSky = findViewById<TextView>(R.id.currentSky)
        currentSky.text = getSky(realtime.text).info
        val currentAQI = findViewById<TextView>(R.id.currentAQI)
        currentAQI.text = "空气指数 ${realtime.vis.toInt()}"
        val nowLayout = findViewById<RelativeLayout>(R.id.nowLayout)
        nowLayout.setBackgroundResource(getSky(realtime.text).bg)

        //填充forecast.xml布局中的数据
        val forecastLayout = findViewById<LinearLayout>(R.id.forecastLayout)
        forecastLayout.removeAllViews()
        val days = daily.size
        //遍历每一个预测天气
        for (i in 0 until days) {
//            Log.d("Text","${daily[i]}")
            //天气描述 晴等
            val temperature = temperature(daily[i].tempMax,daily[i].tempMin)
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val dateInfo = view.findViewById<TextView>(R.id.dateinfo)
            val SkyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val SkyInfo = view.findViewById<TextView>(R.id.skyinfo)
            val temperatureinfo = view.findViewById<TextView>(R.id.temperatureinfo)
            dateInfo.text = daily[i].fxDate
            SkyIcon.setImageResource(getSky(daily[i].textDay).icno)
            SkyInfo.text = daily[i].textDay
            temperatureinfo.text = "${temperature.tempMin.toInt()} ~ ${temperature.tempMax.toInt()} ℃"
            forecastLayout.addView(view)
        }

        //填充life_item.xm布局中的数据
        val lifeItem = lifeNum(daily[0].precip,daily[0].humidity,daily[0].uvIndex,daily[0].vis)
        val coldRiskText = findViewById<TextView>(R.id.coldRiskText)
        val dressingText = findViewById<TextView>(R.id.dressingText)
        val ultravioletText = findViewById<TextView>(R.id.ultravioletText)
        val carWashingText = findViewById<TextView>(R.id.carWashingText)
        val weatherLayout = findViewById<ScrollView>(R.id.weatherLayout)
        coldRiskText.text = lifeItem.precip
        dressingText.text = lifeItem.humidity
        ultravioletText.text = lifeItem.uvIndex
        carWashingText.text = lifeItem.vis
        weatherLayout.visibility = View.VISIBLE
    }
}