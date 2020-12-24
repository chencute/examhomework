package com.zt.endexam.logic.model

//封装实时天气和未来几天天气状况
data class Weather(val realtime:RealtimeResponse.Now,val daily: List<DailyResponse.Daily>)