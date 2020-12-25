package com.zt.endexam.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {

    //创建viewModel
    val viewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(PlaceViewModel::class.java)
    }
    //创建适配器
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(viewModel.isPlaceSaved()) {
//            val place = viewModel.getSavedPlace()
//            val intent = Intent(context,WeatherActivity::class.java).apply {
//                putExtra("cityid",place.id)
//                putExtra("placeName",place.name)
//            }
//            startActivity(intent)
////            activity?.finish()
//            return
//        }

        val layoutManager = LinearLayoutManager(activity)
        val recyclerView:RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        //将请求后的数据放于适配器中
        adapter = PlaceAdapter(this,viewModel.locationList)
        recyclerView.adapter = adapter
        val searchPlaceEdit = view.findViewById<EditText>(R.id.searchPlaceEdit)
        val bgImageView = view.findViewById<ImageView>(R.id.bgImageView)
        //监测编辑框的文本内容变化情况
        searchPlaceEdit.addTextChangedListener {editable ->
            //获取编辑框内容
            val content = editable.toString()
            if(content.isNotEmpty()) {
                //请求数据
                viewModel.searchLocations(content)
            } else {
                //为空展示背景图片
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.locationList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        //观察请求的数据变化
        viewModel.locationLiveData.observe(this, Observer {result ->
            val locations= result.getOrNull()
            if(locations != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.locationList.clear()
                viewModel.locationList.addAll(locations)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}


