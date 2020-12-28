package com.zt.endexam.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zt.endexam.logic.model.GameModel.Score

class gameViewModel : ViewModel() {

    val scoreList =  MutableLiveData<MutableList<Score>>()

}