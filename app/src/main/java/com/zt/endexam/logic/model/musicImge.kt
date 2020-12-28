package com.zt.endexam.logic.model

import android.util.Log
import com.zt.endexam.R

class  musicImge(val bg:Int)

private val image = mapOf(
    "南山南" to musicImge(R.drawable.music_nanshannan),
    "安河桥" to musicImge(R.drawable.music_anheqiao),
    "天外来物" to musicImge(R.drawable.music_tianwailaiwu),
    "红豆" to musicImge(R.drawable.music_hongdou),
    "归去来兮" to musicImge(R.drawable.muaic_guiqulaixi),
    "等空车的人" to musicImge(R.drawable.music_dengkongchederen),
    "猪之歌" to musicImge(R.drawable.music_zhuzhige),
    "演员" to musicImge(R.drawable.music_yanyuan),
    "绅士" to musicImge(R.drawable.music_shengshi),
    "封存" to musicImge(R.drawable.music_fengcun),
    "数羊" to musicImge(R.drawable.music_shuyang)
)

fun getMusicImge(musicname:String):musicImge {
    return image[subMusicName(musicname)] ?: image["南山南"]!!
}

/**
 * 获取字符串中存在的(的第一个位置
 */
fun getMusicIndex(musicname: String):Int {
    for(i in 0 until musicname.length) {
        if(musicname[i] == '（') {
            return i
        }
    }
    return -1
}

/**
 * 截取字符串
 */
fun subMusicName(musicname: String):String {
    if (getMusicIndex(musicname) == -1) {
        return musicname
    } else {
        return musicname.substring(0, getMusicIndex(musicname))
    }
}