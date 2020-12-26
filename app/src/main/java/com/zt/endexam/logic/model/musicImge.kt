package com.zt.endexam.logic.model

import com.zt.endexam.R

class  musicImge(val bg:Int)

private val image = mapOf(
    "南山南" to musicImge(R.drawable.music_nanshannan),
    "安河桥（Cover 宋冬野）" to musicImge(R.drawable.music_anheqiao),
    "天外来物" to musicImge(R.drawable.music_tianwailaiwu),
    "红豆（翻自 方大同）" to musicImge(R.drawable.music_hongdou),
    "归去来兮（翻自 花粥）" to musicImge(R.drawable.muaic_guiqulaixi),
    "等空车的人" to musicImge(R.drawable.music_dengkongchederen),
    "猪之歌" to musicImge(R.drawable.music_zhuzhige),
    "演员" to musicImge(R.drawable.music_yanyuan),
    "绅士" to musicImge(R.drawable.music_shengshi),
    "封存" to musicImge(R.drawable.music_fengcun),
    "数羊" to musicImge(R.drawable.music_shuyang)
)

fun getMusicImge(musicname:String):musicImge {
    return image[musicname] ?: image["南山南"]!!
}