package com.zt.endexam.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.model.Location
import com.zt.endexam.ui.weather.WeatherActivity

class PlaceAdapter (private val fragment:PlaceFragment,private val locationList:List<Location>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view:View) :RecyclerView.ViewHolder(view) {
        val placeName:TextView = view.findViewById(R.id.placeName)
        val placeAddress:TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = locationList[position]

            //如果当前的activity是WeatherActivity则关掉滑动菜单 并重新获取数据
            val activity = fragment.activity
            if(activity is WeatherActivity) {
                val drawerLayout = activity.findViewById<DrawerLayout>(R.id.drawerLayout)
                drawerLayout.closeDrawers()
                activity.viewModel.cityid = place.id
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            } else {
                val intent = Intent(parent.context,WeatherActivity::class.java).apply {
                    putExtra("cityid",place.id)
                    putExtra("placeName",place.name)
                }
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationList[position]
        holder.placeName.text = location.name
        holder.placeAddress.text = location.country + " " + location.adm1 + " " + location.adm2
    }

}