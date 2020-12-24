package com.zt.endexam.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.Location

/**
 * placeui对应的viewmodel
 */
class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    //对页面上显示的城市数据进行缓存
    val locationList = ArrayList<Location>()

    //构造新的LiveData观察对象
    val locationLiveData = Transformations.switchMap(searchLiveData) {location ->
        Repository.serachPlaces(location)
    }

    //每次数据发生变化 调用该方法 则会触发 新的liveData对象的产生
    fun searchLocations(location:String) {
        searchLiveData.value = location
    }
}